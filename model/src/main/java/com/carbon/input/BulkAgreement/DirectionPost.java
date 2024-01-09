package com.carbon.input.BulkAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 定向报价表单
public class DirectionPost implements Serializable {
    private String id;
    private Timestamp time;
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String accountType; // 账户类型
    private String account; // 账户
    private String flowType; // 买入/卖出
    private Double price; // 委托价格
    private Double amount; // 委托数量
    private String directionClient; // 定向客户
    private String operatorCode; // 操作员代码
    private String status; // 状态
}