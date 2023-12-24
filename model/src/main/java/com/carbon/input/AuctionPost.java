package com.carbon.intput;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 单向竞价报价表单
public class AuctionPost {
    private String id;
    private Date time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private String price;
    private String operatorCode; // 操作员代码
}