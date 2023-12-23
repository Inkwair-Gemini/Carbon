package com.carbon.po;

// 客户交易配额
class ClientTradeQuota {
    private String accoundId; // 客户Id
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private double amount; // 总数量
    private double availableQuotaAmount; // 可用数量
    private double unavailableQuotaAmount; // 冻结数量
}