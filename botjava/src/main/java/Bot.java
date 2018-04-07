import java.util.Map;
import java.util.TreeMap;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class Bot extends TelegramLongPollingBot {

  private Map<Long, User> userMap = new TreeMap<>();

  public static void main(String[] args) {
    ApiContextInitializer.init();
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

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

    containsUser(chatId);
    checkTextMsg(chatId, textMsg);
    sendNews(msg, userMap);
  }

  private void checkTextMsg(Long chatId, String messageText) {
    User user = userMap.get(chatId);
    if (messageText.equals("start notification")) {
      user.setStatus(true);
    } else if (messageText.equals("stop notification")) {
      user.setStatus(false);
    }
  }

  private void sendNews(Message msg, Map<Long, User> userMap) {
    String dateMsg = DateConverter.getCurrentDate();
    System.out.println(dateMsg);
    String weatherMsg = WeatherAPI.getWeather();
    String rateMsh = ExchangeRateAPI.getRate();

    userMap.forEach((key, value) -> {
      if (value.getStatus()) {
        System.out.println("Send message");
        sendMsg(msg, weatherMsg);
        sendMsg(msg, rateMsh);
      }
    });

  }


  private void sendMsg(Message msg, String sendText) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
    sendMessage.setChatId(msg.getChatId().toString());
    //sendMessage.setReplyToMessageId(msg.getMessageId());
    sendMessage.setText(sendText);
    try {
      sendMessage(sendMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }


  private void containsUser(Long userId) {
    if (!userMap.containsKey(userId)) {
      User user = new User(userId, false);
      userMap.put(userId, user);
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
