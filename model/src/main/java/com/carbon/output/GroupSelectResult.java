package com.carbon.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 群组报价查询
public class GroupSelectResult {
    private String id;
    private Date time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private String flowType;
    private Double currentPrice;
    private Double currentAmount;
    private Double currentBalance; // 当前金额
    private String groupId; // 群组Id
    private String state; // 报价状态
    private String enquiryState; // 洽谈状态
}