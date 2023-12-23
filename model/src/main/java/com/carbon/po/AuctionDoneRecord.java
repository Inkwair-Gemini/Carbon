package com.carbon.po;

// 单向竞价成交记录
public class AuctionDoneRecord {
    private String id;
    private Date time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private double finallyBalance; // 最终报价金额
    private String requestClient; // 卖方客户
    private String purchaserClient; // 买方客户
}