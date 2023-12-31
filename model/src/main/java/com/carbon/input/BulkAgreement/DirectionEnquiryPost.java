package com.carbon.input.BulkAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 定向洽谈报价表单
public class DirectionEnquiryPost implements Serializable {
    private String id;
    private Timestamp time;
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String flowType;
    private double price;
    private double amount;
    private String directionPostId; // DriectionPostId
    private String directionClient; // 定向客户
    private String operatorCode; // 操作员代码
}