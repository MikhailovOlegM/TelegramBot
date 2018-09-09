package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.logging.Logger;

public class ExchangeRateAPI {

  private static final Logger LOG = Logger.getLogger(WeatherAPI.class.getName());

  public static String getRate() {
    StringBuilder rateMsg = new StringBuilder();

    String url = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    String response = null;
    try {
      response = RequestApi.getResponse(url);
    } catch (Exception e) {
      LOG.severe(e.toString());
      return "Error on privatBank rate";
    }

    Gson gson = new Gson();

    JsonArray jsonArray = gson.fromJson(response, JsonArray.class);
    jsonArray.forEach(element -> {
      JsonObject jsonObj = element.getAsJsonObject();
      rateMsg.append("Валюта: ").append(jsonObj.get("ccy")).append(" -> ")
          .append(jsonObj.get("base_ccy")).append(" | Покупка: ").append(jsonObj.get("buy"))
          .append(" | Продажа: ").append(jsonObj.get("sale")).append("$ \n");
    });

    return rateMsg.toString();
  }
}
