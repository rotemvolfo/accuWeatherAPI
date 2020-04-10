package weather.services.factory;


import adapters.weather.app.AccuWeather;
import adapters.weather.app.AccuWeatherAdapter;
import adapters.weather.app.IWeatherApi;

public class WeatherServicesFactory {

    public static IWeatherApi getService(String providerType) {

        if (providerType.equalsIgnoreCase("AccuWeather"))
            return new AccuWeatherAdapter(new AccuWeather());
        else
            return null;

    }

}
