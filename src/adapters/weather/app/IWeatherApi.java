package adapters.weather.app;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public interface IWeatherApi {

  JSONObject getWeatherConditionsByLocation(String param , int zipCode);
  JSONObject createJsonInBigPandaFormat(String alertType,String weatherInfo);
}
