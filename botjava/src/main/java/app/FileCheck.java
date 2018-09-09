package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.StringJoiner;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class FileCheck extends Thread {

  String urlPathToFile = "/media/hdd/Documents/bot notification/TelegramBot/botjava/application_log.txt.0";
  FileTime modificationTime = null;
  private static final Logger LOG = Logger.getLogger(FileCheck.class.getName());
  private boolean runStatus = true;
  private Bot bot = new Bot();

  private int lineCounter = 0;

  @Override
  public void run() {
    runStatus = true;
    while (runStatus) {
      Path paths = Paths.get(urlPathToFile);
      try {
        FileTime tmpTime = Files.getLastModifiedTime(paths);
        if (tmpTime.equals(modificationTime)) {
          continue;
        }

        modificationTime = tmpTime;
        StringJoiner message = new StringJoiner("\n");
        int counter = 0;

        Stream<String> stream = Files.lines(paths);
        for (Object line : stream.toArray()) {
          counter++;
          if (isException(line.toString()) && counter > lineCounter) {
            lineCounter = counter;
            message.add(line.toString());
          }
        }

        counter = 0;
        String result = message.toString();
        if (!result.isEmpty()) {
          this.sendMsg("250770959", result);
        }

      } catch (IOException e) {
        LOG.severe(e.toString());
      }
    }
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

  private boolean isException(String line) {
    return line.toLowerCase().contains("exception") || line.contains("error") || line
        .contains("warning") || line.contains("at ");
  }

  public void stopThread() throws Throwable {
    this.stop();
  }


}
