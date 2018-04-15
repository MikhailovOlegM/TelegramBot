package app;

import java.util.Map;
import java.util.TreeMap;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import service.DatabaseConnector;
import service.TimeScheduler;

public class Bot extends TelegramLongPollingBot {

  private DatabaseConnector databaseConnector = new DatabaseConnector();

  public static void main(String[] args) {
    System.out.println("Bot start...");
    ApiContextInitializer.init();
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    TimeScheduler.initial();

    try {
      telegramBotsApi.registerBot(new Bot());
    } catch (TelegramApiRequestException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void onUpdateReceived(Update update) {
    Message msg = update.getMessage();
    if (msg == null && !msg.hasText()) {
      return;
    }

    Long chatId = msg.getChatId();
    String textMsg = msg.getText();

    org.telegram.telegrambots.api.objects.User userTelegram = msg.getFrom();
    System.out.println("User: " + userTelegram.getUserName() + ". Message: " + textMsg);

    databaseConnector
        .addUser(new User(String.valueOf(userTelegram.getId()), userTelegram.getUserName(), false));
    checkTextMsg(chatId, textMsg);
  }

  private void checkTextMsg(Long chatId, String messageText) {
    if (messageText.equals("start notification")) {
      databaseConnector.updateUser(true, String.valueOf(chatId));
    } else if (messageText.equals("stop notification")) {
      databaseConnector.updateUser(false, String.valueOf(chatId));
    }
  }

  @Override
  public String getBotUsername() {
    return "@OlegBlackBot";
  }

  @Override
  public String getBotToken() {
    return "371686725:AAGngbC4U3AtZ2QgtT2UhpMYxNiJEj6K4hQ";
  }
}
