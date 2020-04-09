package integration.big.panda;
import http.utility.HttpUtility;
import org.json.JSONException;
import org.json.JSONObject;


public class BigPanda {

    private final String  BASE_URL_BIG_PANDA = "https://api.bigpanda.io/data/v2/alerts";
    private final String APP_KEY = "f2d102014e1a2b7dfcf8cf0065b10656";
    private final String TOKEN="bearer ca1457c70f923c659de4f8281ecae2d2";



    public void sendAlert(JSONObject weatherObj ){
        if(weatherObj == null)
            return;
       try
        {
           weatherObj.put("app_key", this.APP_KEY);
           HttpUtility.postRequest(this.BASE_URL_BIG_PANDA, this.TOKEN, weatherObj);
        }
        catch( JSONException exception)
        {
            System.out.println("Big Panda - adding token to json object failed ");
        }
    }


}
