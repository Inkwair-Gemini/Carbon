package com.carbon.po;

// 挂牌交易成交记录
class ListingDoneRecord {
    private String id;
    private Date time;
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String flowType; // 买入/卖出
    private double dealPrice; // 成交价格
    private double dealAmount; // 成交数量
    private double dealBalance; // 成交金额
    private String listingClient; // 挂牌方
    private String delistingClient; // 摘牌方
}