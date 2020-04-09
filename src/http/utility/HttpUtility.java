package http.utility;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtility {



    private static String returnResponseContent(HttpURLConnection connection)
    {
        String readLine = null;
        StringBuffer response = new StringBuffer();
        try
        {
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            return response.toString();
        }
        catch (IOException e) {
            System.out.println("Failed to read response object");
        }
        return null;
    }


    public static String getRequest(String url) throws MalformedURLException {
        URL urlForGetRequest = new URL(url);
        String readLine = null;
      try {
          HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
          connection.setRequestMethod("GET");
          int responseCode = connection.getResponseCode();
          if (responseCode == HttpURLConnection.HTTP_OK)
              return returnResponseContent(connection);
      }
      catch(ConnectException connectException) {
          System.out.println("Get request failed -"+url+connectException.getMessage());
      }
      catch (IOException exception){
          exception.getMessage();
      }
      return null;
    }

    public static void postRequest(String uri ,String token, JSONObject body )
    {
        try {
            HttpClient client = HttpClient.newBuilder().build(); //creating HttpClient to send requests
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .header("authorization", token)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //handle response here...
            System.out.println(response.statusCode());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
