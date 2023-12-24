package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 群组报价成交记录
public class GroupDoneRecord {
    private String id;
    private Date time;
    private String groupId;
    private String subjectMatterCode;
    private String subjectMatterName;
    private String flowType;
    private Double firstPrice; // 单价
    private Double firstAmount; // 数量
    private Double firstBalance; // 初始报价金额
    private Double finallyPrice;
    private Double finallyAmount;
    private Double finallyBalance; // 最终报价金额
    private String oppositeClient; // 群组中成交客户
    private String operatorCode; // 操作员代码
}