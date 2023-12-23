package com.carbon.output;

// 定向报价查询
class DirectionSelectResult {
    private String id;
    private Date time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private String flowType;
    private double currentPrice;
    private double currentAmount;
    private double currentBalance; // 当前金额
    private String directionClient; // 定向客户
    private String state; // 报价状态
    private String enquiryState; // 洽谈状态
}