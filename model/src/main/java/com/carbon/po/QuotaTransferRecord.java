package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 配额转入转出流水
public class QuotaTransferRecord {
    private String id;
    private Timestamp time;
    private String clientId;
    private String clientName;
    private String operatorCode;
    private String type; // 出/入
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private Double actualAmount; // 划转数量
    private Double possessAmount; // 持有数量
    private Double availableQuotaAmount; // 可用数量
    private String quotaAccount; // 交易账户
    private String status; // 划入划出状态
    private String description; // 备注
}