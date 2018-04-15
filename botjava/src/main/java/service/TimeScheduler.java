package service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeScheduler {

  public static void initial() {
    LocalDateTime localNow = LocalDateTime.now();
    ZoneId currentZone = ZoneId.of("Europe/Kiev");
    ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
    ZonedDateTime zonedNext5;
    ZonedDateTime zonedNext6;
    zonedNext5 = zonedNow.withHour(6).withMinute(0).withSecond(0);
    zonedNext6 = zonedNow.withHour(17).withMinute(0).withSecond(0);
    System.out.println("time: " + zonedNow);
    if (zonedNow.compareTo(zonedNext5) > 0) {
      zonedNext5 = zonedNext5.plusDays(1);
    }
    if (zonedNow.compareTo(zonedNext6) > 0) {
      zonedNext6 = zonedNext6.plusDays(1);
    }

    Duration duration1 = Duration.between(zonedNow, zonedNext5);
    Duration duration2 = Duration.between(zonedNow, zonedNext6);
    long initalDelay1 = duration1.getSeconds();
    long initalDelay2 = duration2.getSeconds();

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(new BotSender(), initalDelay1,
        24 * 60 * 60, TimeUnit.SECONDS);
    scheduler.scheduleAtFixedRate(new BotSender(), initalDelay2,
        24 * 60 * 60, TimeUnit.SECONDS);
  }
}
