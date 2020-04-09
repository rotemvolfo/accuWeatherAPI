package adapters.weather.app;

import http.utility.HttpUtility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AccuWeatherAdapter implements IWeatherApi {

     private  AccuWeather _accuWeather;

    public AccuWeatherAdapter(AccuWeather accuWeather){
        _accuWeather=new AccuWeather();
    }
    @Override
    public JSONObject createJsonInBigPandaFormat(String cityWeather){
        JSONObject jsonInBigPandaFormat= null;
        try {
            jsonInBigPandaFormat = _accuWeather.createJsonInBigPandaFormat(cityWeather);
        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        }
        return jsonInBigPandaFormat;
    }
    @Override
    public JSONObject getWeatherConditionsByLocation(String param)
    {
        JSONObject cityWeatherJson=null;
        try {
            String locationKey = _accuWeather.returnLocationKeyFromSearchTextApi(param);
            if(locationKey == null) {
                cityWeatherJson=this.createJsonInBigPandaFormat("Location key empty");
            }
            else
            {
                String response = HttpUtility.getRequest(this._accuWeather.getBaseUrl() + "/forecasts/v1/daily/1day/" + locationKey + "?apikey=" + this._accuWeather.getAppKey() + "&details=true");
                if(response == null)
                    cityWeatherJson=this.createJsonInBigPandaFormat("Forecasts api failed");
                else
                cityWeatherJson = this.createJsonInBigPandaFormat(response);
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return cityWeatherJson;
    }
}
