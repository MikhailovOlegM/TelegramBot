package service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {

  public static String getCurrentDate() {
    String result;
    Calendar calendar = Calendar.getInstance();
    result = "Сегодня " + calendar.get(Calendar.DAY_OF_MONTH) + " " + getMonth(
        calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR) + " года";

    return result;
  }


  private static String getMonth(int numberOfMonth) {
    String fMonth;
    switch (numberOfMonth) {
      case 0:
        return fMonth = "января";
      case 1:
        return fMonth = "февраля";
      case 2:
        return fMonth = "марта";
      case 3:
        return fMonth = "апреля";
      case 4:
        return fMonth = "мае";
      case 5:
        return fMonth = "июня";
      case 6:
        return fMonth = "июля";
      case 7:
        return fMonth = "августа";
      case 8:
        return fMonth = "сентября";
      case 9:
        return fMonth = "октября";
      case 10:
        return fMonth = "ноября";
      case 11:
        return fMonth = "декабря";
    }
    return null;
  }

}
