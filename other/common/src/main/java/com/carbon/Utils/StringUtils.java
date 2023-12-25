package com.carbon.Utils;

public class StringUtils {
    public static boolean isEmpty(String s){
        if(s.length()==0 && s==null && s.equals(""))
            return true;
        else return false;
    }
    public static boolean isNotEmpty(String s){
        return !isEmpty(s);
    }
}
