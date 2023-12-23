package com.carbon.po;

// 群组报价成交记录
class GroupDoneRecord {
    private String id;
    private Date time;
    private String groupId;
    private String subjectMatterCode;
    private String subjectMatterName;
    private String flowType;
    private double firstPrice; // 单价
    private double firstAmount; // 数量
    private double firstBalance; // 初始报价金额
    private double finallyPrice;
    private double finallyAmount;
    private double finallyBalance; // 最终报价金额
    private String oppositeClient; // 群组中成交客户
    private String operatorCode; // 操作员代码
}