package com.carbon.input;

// 单向竞价委托单
class AuctionRequest {
    private String id;
    private String time; // 申请时间
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String accountType; // 账户类型
    private String quotaAccount; // 配额账户
    private double price; // 委托价格
    private double amount; // 委托数量
    private String operatorCode; // 操作员代码
    private String status; // 状态
}