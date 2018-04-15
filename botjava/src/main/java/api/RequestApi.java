package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestApi {

  public static String getResponse(String url) {
    URL obj = null;
    StringBuilder response = null;
    try {
      obj = new URL(url);

      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod("GET");

      con.setRequestProperty("app.User-Agent", "Mozilla/5.0");

      int responseCode = con.getResponseCode();

      BufferedReader in = new BufferedReader(
          new InputStreamReader(con.getInputStream()));
      String inputLine;
      response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return response.toString();

  }
}
