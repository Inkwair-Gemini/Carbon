package com.carbon.po;

// 单向竞价商品
class AuctionQuota {
    private String id;
    private String time; // 平台发布时间
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private double price; // 委托价格
    private double amount; // 委托数量
    private String totalBalance; // 总价
    private String clientId;
    // 剩余时间
    private String status; // 状态
}