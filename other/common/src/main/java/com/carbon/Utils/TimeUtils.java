package com.carbon.Utils;

import java.sql.Timestamp;

public class TimeUtils {
    public static Timestamp getCurrentTimestamp(){
        Timestamp t = new Timestamp(System.currentTimeMillis());
        return t;
    }
}
