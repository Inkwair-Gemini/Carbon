package com.carbon.utils.toolUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    public static Timestamp getCurrentTimestamp(){
        Timestamp t = new Timestamp(System.currentTimeMillis());
        return t;
    }
    public static String getCurrentTimeString(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
