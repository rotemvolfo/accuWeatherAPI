package adapters.weather.app;

import exceptions.ThirdPartyConnectionException;
import http.utility.HttpUtility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;

public class AccuWeatherAdapter implements IWeatherApi {

     private  AccuWeather _accuWeather;

    public AccuWeatherAdapter(AccuWeather accuWeather){
        _accuWeather=accuWeather;
    }

    @Override
    public JSONObject createJsonInBigPandaFormat(String alertType,String cityWeather ){
        JSONObject bigPandaFormat= null;
        try {
            bigPandaFormat = _accuWeather.createJsonInBigPandaFormat(alertType,cityWeather);
        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        }
        return bigPandaFormat;
    }

    @Override
    public JSONObject getWeatherConditionsByLocation(String param ,int zipCode)
    {
        JSONObject cityWeatherJson=null;
        try {
            String locationKey = _accuWeather.returnLocationKeyFromSearchTextApi(param,zipCode);
            String response =  _accuWeather.getWeatherForecasts(locationKey);
            cityWeatherJson = this.createJsonInBigPandaFormat("Weather alert",response);
        }
        catch (ThirdPartyConnectionException ex){ //  exception with the error code from the api request will be send as alert to Big Panda
            cityWeatherJson=this.createJsonInBigPandaFormat("Third Party Connection failed", ex.getMessage());
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return cityWeatherJson;
    }
}
