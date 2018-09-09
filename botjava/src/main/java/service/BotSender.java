package service;

import api.ExchangeRateAPI;
import api.WeatherAPI;
import app.Bot;
import app.User;
import java.util.List;
import java.util.logging.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class BotSender implements Runnable {

  private Bot bot = new Bot();
  private static final Logger LOG = Logger.getLogger(BotSender.class.getName());

  @Override
  public void run() {
    DatabaseConnector database = new DatabaseConnector();

    String dateMsg = DateConverter.getCurrentDate();
    System.out.println(dateMsg);
    String weatherMsg = WeatherAPI.getWeather();
    String rateMsh = ExchangeRateAPI.getRate();
    List<User> userList = database.getUsers();
    userList.forEach(user -> {
      if (user.getStatus()) {
        LOG.info("Send message to: " + user);
        sendMsg(user.getId(), weatherMsg);
        sendMsg(user.getId(), rateMsh);
      }
    });
  }


  private void sendMsg(String chatId, String sendText) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
    sendMessage.setChatId(chatId);
    sendMessage.setText(sendText);
    try {
      bot.execute(sendMessage);
    } catch (TelegramApiException e) {
      LOG.severe(e.toString());
    }
  }
}
