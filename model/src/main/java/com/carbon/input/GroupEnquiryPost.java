package com.carbon.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 群组洽谈报价表单
public class GroupEnquiryPost implements Serializable {
    private String id;
    private Timestamp time;
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String flowType;
    private double price;
    private double amount;
    private String groupId; // 群组Id
    private String operatorCode; // 操作员代码
}