package com.carbon.po;

// 出入金流水
class DepositAndWithdrawalRecord {
    private String id;
    private String date;
    private String time;
    private String capitalAccount; // 资金账户Id
    private String operatorCode;
    private String type; // 出/入
    private double actualAmount;
    private double endingBalance;
    private double endingAvailableBalance;
}