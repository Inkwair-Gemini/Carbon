package com.carbon.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DirectionAndGroupEnquiryPostResult {
    private String id;
    private Timestamp time;
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String flowType;
    private String price;
    private String amount;
    private String directionOrGroupId; //对方
    private String operatorCode; // 操作员代码
}
