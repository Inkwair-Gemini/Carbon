package com.carbon.po;

// 出入金申请记录
class DepositAndWithdrawalRequestRecord {
    private String id;
    private String date;
    private String time;
    private String initiator; // 发起员
    private String bindBank; // 银行Id
    private String bankAccountId; // 银行账户Id
    private String type; // 出/入
    private String requestState; // 申请状态
    private double actualAmount; // 发生金额
    private String description; // 备注
}