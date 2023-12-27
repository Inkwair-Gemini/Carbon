package com.carbon.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor

// 定向报价查询
public class DirectionSelectResult {
    private String id;
    private Timestamp time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private String flowType;
    private Double currentPrice;
    private Double currentAmount;
    private Double currentBalance; // 当前金额
    private String directionClient; // 定向客户
    private String state; // 报价状态
    private String enquiryState; // 洽谈状态
}