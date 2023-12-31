package com.carbon.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DirectionAndGroupPostResult implements Serializable {
    private String id;
    private Timestamp time;
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String accountType; // 账户类型
    private String quotaAccount; // 配额账户
    private String flowType; // 买入/卖出
    private Double price; // 委托价格
    private Double amount; // 委托数量
    private String clientOrGroupId; // 对方
    private String operatorCode; // 操作员代码
    private String status; // 状态
}
