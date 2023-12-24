package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 定向报价成交记录
public class DirectionDoneRecord {
    private String id;
    private Date time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private String flowType;
    private Double firstPrice; // 单价
    private Double firstAmount; // 数量
    private Double firstBalance; // 初始报价金额
    private Double finallyPrice;
    private Double finallyAmount;
    private Double finallyBalance; // 最终报价金额
    private String buyClient; // 买方
    private String sellClient; // 卖方
}