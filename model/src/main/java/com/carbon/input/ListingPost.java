package com.carbon.input;

// 挂牌表单
class ListingPost {
    private String id;
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String accountType; // 账户类型
    private String quotaAccount; // 配额账户
    private String flowType; // 买入/卖出
    private double price; // 委托价格
    private double amount; // 委托数量
    private String operatorCode; // 操作员代码
    private String status; // 状态
}