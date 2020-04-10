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


    public static String getRequest(String url) throws MalformedURLException {
        String readLine = null;
        InputStreamReader responseStream=null;
        int responseCode;
      try
      {
          URL urlForGetRequest = new URL(url);
          HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
          connection.setRequestMethod("GET");
          responseCode = connection.getResponseCode();
          StringBuffer response = new StringBuffer();

          if (responseCode == HttpURLConnection.HTTP_OK)
            responseStream= new InputStreamReader(connection.getInputStream());
          else
            responseStream= new InputStreamReader(connection.getErrorStream());

          BufferedReader in = new BufferedReader(responseStream);
          while ((readLine = in.readLine()) != null) {
              response.append(readLine);
          }
          in.close();
          return response.toString();
      }
      catch (IOException exception){
          System.out.println("Failed to read response stream");
      }
      return null;
    }

    public static void postRequest(String uri ,String token, JSONObject body )
    {
        try
        {
            HttpClient client = HttpClient.newBuilder().build(); //creating HttpClient to send requests
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .header("authorization", token)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("statusCode: "+response.statusCode() +" url "+uri+" body: "+response.body());
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
