package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 客户交易配额
public class ClientTradeQuota implements Serializable {
    private String quotaAccountId; // 配额账户Id
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private Double amount; // 总数量
    private Double availableQuotaAmount; // 可用数量
    private Double unavailableQuotaAmount; // 冻结数量
}