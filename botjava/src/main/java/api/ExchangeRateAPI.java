package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ExchangeRateAPI {

  public static String getRate() {
    StringBuilder rateMsg = new StringBuilder();

    String url = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    String response = RequestApi.getResponse(url);

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
