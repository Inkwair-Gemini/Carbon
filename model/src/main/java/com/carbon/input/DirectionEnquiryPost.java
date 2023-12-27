package com.carbon.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 定向洽谈报价表单
public class DirectionEnquiryPost {
    private String id;
    private Timestamp time;
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String flowType;
    private String price;
    private String amount;
    private String directionClient; // 定向客户
    private String operatorCode; // 操作员代码
}