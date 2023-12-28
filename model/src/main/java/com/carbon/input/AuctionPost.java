package com.carbon.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor

// 单向竞价报价表单
public class AuctionPost {
    private String id;
    private Timestamp time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private Double price;
    private String operatorCode; // 操作员代码
}