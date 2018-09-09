package app;

import api.ExchangeRateAPI;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import service.DatabaseConnector;
import service.TimeScheduler;

public class Bot extends TelegramLongPollingBot {

  private DatabaseConnector databaseConnector = new DatabaseConnector();
  private static final Logger LOG = Logger.getLogger(Bot.class.getName());

  FileCheck fileCheck;

  public static void main(String[] args) {
    System.out.println("Bot start...");
    LOG.info("Bot start...");
    try {
      LogManager.getLogManager().readConfiguration(
          Bot.class.getResourceAsStream("/logging.properties"));
    } catch (IOException e) {
      System.err.println("Could not setup logger configuration: " + e.toString());
    }

    ApiContextInitializer.init();
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    TimeScheduler.initial();

    try {
      telegramBotsApi.registerBot(new Bot());
    } catch (TelegramApiRequestException e) {
      LOG.severe(e.toString());
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
    LOG.info("User: " + userTelegram.getId() + " " + userTelegram.getFirstName() + userTelegram
        .getLastName() + ". Message: "
        + textMsg);

    databaseConnector
        .addUser(new User(String.valueOf(userTelegram.getId()),
            userTelegram.getLastName() + " " + userTelegram.getFirstName(), false));
    checkTextMsg(chatId, textMsg);
  }

  private void checkTextMsg(Long chatId, String messageText) {
    if (messageText.equals("log")) {
      this.checkLogFiles();
      return;
    } else if (messageText.equals("log stop")) {
      this.stopCheck();
    } else if (messageText.equals("exc")) {
        throw new NullPointerException();
    }else if (messageText.equals("exc2")){
      throw new IllegalArgumentException();
    }

    if (messageText.equals("start")) {
      databaseConnector.updateUser(true, String.valueOf(chatId));
    } else if (messageText.equals("stop")) {
      databaseConnector.updateUser(false, String.valueOf(chatId));
    } else if (messageText.equals("rate")) {
      SendMessage sendMessage = new SendMessage();
      sendMessage.enableMarkdown(true);
      sendMessage.setChatId(chatId);
      sendMessage.setText(ExchangeRateAPI.getRate());
      try {
        execute(sendMessage);
      } catch (TelegramApiException e) {
        LOG.severe(e.toString());
      }
    }
  }

  private void stopCheck() {
    try {
      fileCheck.stopThread();
    } catch (Throwable throwable) {
      LOG.severe(throwable.getMessage());
    }
  }

  private void checkLogFiles() {
    fileCheck = new FileCheck();
    fileCheck.start();
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
