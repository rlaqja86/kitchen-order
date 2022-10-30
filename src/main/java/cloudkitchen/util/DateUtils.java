package cloudkitchen.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Integer getDifference(Date start, Date end) {
        return Math.toIntExact((end.getTime() - start.getTime()) / 1000);
    }

    public static Date addMilliseconds(Date date, Integer seconds) {
        Date newDate = new Date();
        newDate.setTime(date.getTime() + seconds);

        return newDate;
    }


}
