import adapters.weather.app.AccuWeather;
import adapters.weather.app.AccuWeatherAdapter;
import exceptions.ThirdPartyConnectionException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.*;
import org.json.simple.parser.JSONParser;


public class testWeatherObject {

    @Mock
    private AccuWeather accuWeatherMock;
    @InjectMocks
    private AccuWeatherAdapter service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWeatherObject() throws IOException {

        JSONParser parser = new JSONParser();
        String weatherInfoPath = "tests/weatherInfo";
        String pathAlert = "tests/alert";

        File weatherInfoF = new File(weatherInfoPath);
        String weatherInfoAbsolutePath = weatherInfoF.getAbsolutePath();

        try {

             Object weatherInfo = parser.parse(new FileReader(weatherInfoAbsolutePath));

             when(accuWeatherMock.returnLocationKeyFromSearchTextApi(any(String.class), any(Integer.class))).thenReturn("347629"); // mocking the api calls to the 3rd party (Accuweather)
             when(accuWeatherMock.getWeatherForecasts(any(String.class))).thenReturn( weatherInfo.toString());
             when(accuWeatherMock.createJsonInBigPandaFormat(any(String.class),any(String.class))).thenCallRealMethod();

             JSONObject cityWeatherJson=service.getWeatherConditionsByLocation("san francisco",94193);// the function that been tested

             File  alertF = new File(pathAlert);
             String absolutePathAlert = alertF.getAbsolutePath();
             org.json.simple.JSONObject alert = (org.json.simple.JSONObject)parser.parse(new FileReader(absolutePathAlert));

            assertEquals(alert.get("Link"),cityWeatherJson.get("Link")); // skips the host field
            assertEquals(alert.get("Rain"),cityWeatherJson.get("Rain"));
            assertEquals(alert.get("Snow"),cityWeatherJson.get("Snow"));
            assertEquals(alert.get("Weather Text"),cityWeatherJson.get("Weather Text"));
            assertEquals(alert.get("status"),cityWeatherJson.get("status"));

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ThirdPartyConnectionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}



