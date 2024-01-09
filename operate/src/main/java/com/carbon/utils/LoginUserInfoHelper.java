package com.carbon.utils;

public class LoginUserInfoHelper {
    private static final ThreadLocal<String> clientId = new ThreadLocal();
    private static final ThreadLocal<String> operatorCode = new ThreadLocal();

    public LoginUserInfoHelper() {
    }

    public static void setClientId(String _clientId) {
        clientId.set(_clientId);
    }

    public static String getClientId() {
        return clientId.get();
    }

    public static void removeClientId() {
        clientId.remove();
    }

    public static void setOperatorCode(String _operatorCode) {
        operatorCode.set(_operatorCode);
    }

    public static String getOperatorCode() {
        return operatorCode.get();
    }

    public static void removeOperatorCode() {
        operatorCode.remove();
    }
}
