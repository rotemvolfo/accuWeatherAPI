package adapters.weather.app;

import http.utility.HttpUtility;
import org.json.*;

import java.io.IOException;
import java.util.HashMap;

public class AccuWeather {

    private final String  BASE_URL_ACCU_WEATHER = "http://dataservice.accuweather.com/";
    private final String ACCU_WEATHER_APP_ID = "k2a960rvFFXZoWGGb61IJAO3pGqyUGex";

    public String getAppKey(){
        return this.ACCU_WEATHER_APP_ID;
    }
    public String getBaseUrl(){
        return this.BASE_URL_ACCU_WEATHER;
    }

   public String returnLocationKeyFromSearchTextApi(String city) {
       String locationKey=null;
        try {
          String response = HttpUtility.getRequest(BASE_URL_ACCU_WEATHER + "/locations/v1/cities/search?q=" + city + "&apikey=" + ACCU_WEATHER_APP_ID);
          if(response != null) {
              JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
              locationKey = jsonObject.getString("Key");
          }
      }
      catch (Exception ex){
          System.out.println("AccuWhether Api- Failed to return location Key");
      }
       return locationKey;
   }

   public JSONObject createJsonInBigPandaFormat(String response) throws JSONException {
        JSONObject data = new JSONObject();
       try {
           if(response.equalsIgnoreCase("Location key empty"))
           {
               data.put("host", "Accuweather Locations api ");
               data.put("status", "critical");
               data.put("description", "https GET requeset to Accuweather Locations api failed ");
               return data;
           }
           if(response.equalsIgnoreCase("Forecasts api failed"))
           {
               data.put("host", "Accuweather Forecasts api ");
               data.put("description", "https GET requeset to Accuweather Forecasts api failed");
               data.put("status", "critical");
               return data;
           }
           JSONObject weatherObj = new JSONObject(response);
           data.put("host", "weather-12");
           //------------------------------------------ data from headLine section ------
           JSONObject tempHeadline = weatherObj.getJSONObject("Headline");
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

       } catch (JSONException e) {
           e.printStackTrace();
       }
       return data;
   }
       private String returnStatusByTemperature(Integer temperature) throws JSONException {
           String status=null;
           if(temperature<40 ||temperature>105 )
               status="critical";
           else if(temperature<55 ||temperature>100)
               status="warning";
           else
               status="warning";

           return status;
       }
   }
