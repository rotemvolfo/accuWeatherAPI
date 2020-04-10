package adapters.weather.app;
import exceptions.ThirdPartyConnectionException;
import http.utility.HttpUtility;
import org.json.*;

public class AccuWeather {

    private final String  BASE_URL_ACCU_WEATHER = "http://dataservice.accuweather.com/";
    private final String ACCU_WEATHER_APP_ID = "k2a960rvFFXZoWGGb61IJAO3pGqyUGex";


   public String returnLocationKeyFromSearchTextApi(String city, int zipCode) throws ThirdPartyConnectionException {

       String response=null;
        try {
            response = HttpUtility.getRequest(BASE_URL_ACCU_WEATHER + "/locations/v1/cities/search?q=" + city + "&apikey=" + ACCU_WEATHER_APP_ID);
            JSONArray locationsArr=new JSONArray(response);
            JSONObject location=null;
             for(int i=0;i<locationsArr.length(); i++)
             {
               location = locationsArr.getJSONObject(i);
               if(location.getString("PrimaryPostalCode").length()>0 && location.getInt("PrimaryPostalCode")== zipCode) // PrimaryPostalCode can be Empty or number
                 return  location.getString("Key");
             }
           return null;
      }
      catch (Exception ex){
          throw new ThirdPartyConnectionException(response);
      }
   }

    public String getWeatherForecasts(String locationKey) throws ThirdPartyConnectionException {

        String response=null;
        try {
            response = HttpUtility.getRequest(BASE_URL_ACCU_WEATHER+ "/forecasts/v1/daily/1day/" + locationKey + "?apikey=" + ACCU_WEATHER_APP_ID + "&details=true");
            return response;
        }
        catch (Exception ex){
            throw new ThirdPartyConnectionException(response);
        }
    }
   public JSONObject createJsonInBigPandaFormat(String alertType,String response) throws JSONException {
        JSONObject data = new JSONObject();
       try {
           if(alertType.equalsIgnoreCase("Third Party Connection failed"))
           {
               JSONObject errorObj = new JSONObject(response);
               data.put("host", "Accuweather Connection failed");
               data.put("status", "critical");
               errorObj.keys().forEachRemaining(key -> {
                   try {
                       data.put((String)key,errorObj.getString((String) key));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               });
               return data;
           }
           if(alertType.equalsIgnoreCase("Weather alert")) {
               JSONObject weatherObj = new JSONObject(response);

               //------------------------------------------ data from headLine section ------
               JSONObject tempHeadline = weatherObj.getJSONObject("Headline");
               data.put("host", "weather"+tempHeadline.getLong("EffectiveEpochDate")%10000); // create difference hosts  to allow correlations by zip code
               data.put("Weather Text", tempHeadline.get("Text"));
               data.put("Link", tempHeadline.get("Link"));
               //-----------------------------------------DailyForecasts---------
               JSONObject dailyForecasts = (JSONObject) weatherObj.getJSONArray("DailyForecasts").get(0);
               JSONObject imperialTemp = dailyForecasts.getJSONObject("Temperature");
               JSONObject maxTemp = imperialTemp.getJSONObject("Maximum");
               data.put("Temperature", maxTemp.get("Value"));
               data.put("status", returnStatusByTemperature(maxTemp.getInt("Value")));

               JSONObject day = dailyForecasts.getJSONObject("Day");
               JSONObject snowData = day.getJSONObject("Snow");
               data.put("Snow", snowData.getInt("Value") > 0 ? "Yes" : "No");
               JSONObject rain = day.getJSONObject("Rain");
               data.put("Rain", rain.getInt("Value") > 0 ? "Yes" : "No");
           }

       } catch (JSONException e) {
           e.printStackTrace();
       }
       return data;
   }
       private String returnStatusByTemperature(Integer temperature)
       { //  an alarm exists for a location if the forecast weather meets or exceeds the following thresholds
           String status=null;
           if(temperature<40 ||temperature>105 )
               status="critical";
           else if(temperature<55 ||temperature>100)
               status="warning";
           else
               status="critical"; // should be "OK" , changed to critical in order to send more alerts

           return status;
       }
   }
