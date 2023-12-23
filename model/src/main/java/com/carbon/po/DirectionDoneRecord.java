package com.carbon.po;

// 定向报价成交记录
public class DirectionDoneRecord {
    private String id;
    private Date time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private String flowType;
    private double firstPrice; // 单价
    private double firstAmount; // 数量
    private double firstBalance; // 初始报价金额
    private double finallyPrice;
    private double finallyAmount;
    private double finallyBalance; // 最终报价金额
    private String oppositeClient; // 对方客户
    private String operatorCode; // 操作员代码
}