package cloudkitchen.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class LogGenerator {

   public static SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss.SSS");

    public static void generateLog(String orderEvent, String orderId, String orderName, Date time) {
        System.out.println(orderEvent + " - " + "orderId : " +orderId + " - orderName : " + orderName  +
                 " - time : " + sf.format(time));
    }
}
