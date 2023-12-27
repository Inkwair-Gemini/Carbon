package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 配额交易流水
public class QuotaTradeRecord {
    private String id;
    private Timestamp time;
    private String clientId; // 客户号
    private String clientName; // 客户名称
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String tradeType; // 交易模式
    private String operate; // 操作类型
    private Double actualAmount; // 划转数量
    private Double possessAmount; // 持有数量
    private String quotaAccount; // 交易账户
}