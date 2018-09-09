package api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.logging.Logger;
import service.BotSender;
import service.DateConverter;

public class WeatherAPI {
  private static final Logger LOG = Logger.getLogger(WeatherAPI.class.getName());


  public static String getWeather() {

    String url = "https://api.openweathermap.org/data/2.5/weather?lat=46.48&lon=30.73&APPID=ea4df32bcad15dd978c73c70b40a0935";
    String response = null;
    try{
      response =  RequestApi.getResponse(url);
    }catch (Exception e){
      LOG.severe(e.toString());
      return "Error weather";
    }
    Gson gson = new Gson();
    JsonElement jsonElement = gson.fromJson(response, JsonObject.class);

    JsonElement cityName = jsonElement.getAsJsonObject().get("name");
    JsonElement windSpeed = jsonElement.getAsJsonObject().get("wind").getAsJsonObject()
        .get("speed");
    JsonElement temperature = jsonElement.getAsJsonObject().get("main").getAsJsonObject()
        .get("temp");
    Long tempratureFinal = Math.round(Double.valueOf(temperature.toString()) - 273);
    JsonElement cloudsLevel = jsonElement.getAsJsonObject().get("clouds").getAsJsonObject()
        .get("all");
    JsonElement pressure = jsonElement.getAsJsonObject().get("main").getAsJsonObject()
        .get("pressure");
    JsonElement weatherDescription = jsonElement.getAsJsonObject().get("weather").getAsJsonArray()
        .get(0).getAsJsonObject().get("description");
    String dateMsg = DateConverter.getCurrentDate();

    return dateMsg + "\n" +
        "Город: " + cityName + " \n Общие сведения: " + weatherDescription + " \n Ветер: "
        + windSpeed + " м/с \n Температура: " + tempratureFinal + " \n Уровень облачности: "
        + cloudsLevel + " \n Давление: " + pressure + " мм.рт.ст";
  }
}
