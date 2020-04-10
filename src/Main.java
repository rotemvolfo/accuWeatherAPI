import adapters.weather.app.IWeatherApi;
import integration.big.panda.BigPanda;
import org.json.JSONObject;
import weather.services.factory.WeatherServicesFactory;


public class Main {

        public static void main(String[] args){
        IWeatherApi weatherService= WeatherServicesFactory.getService("AccuWeather");
        JSONObject weatherInfo=weatherService.getWeatherConditionsByLocation("san francisco",94103);
        BigPanda bigPanda = new BigPanda();
        bigPanda.sendAlert(weatherInfo);

    }





}
