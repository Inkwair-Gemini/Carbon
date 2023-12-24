package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 资金交易流水
public class CapitalTradeRecord {
    private String id;
    private Date time;
    private String operatorCode; // 操作员代码
    private String capitalAccount; // 资金账号
    private String type; // 划转类型
    private Double actualAmount; // 发生金额
    private Double endingBalance; // 期后余额
    private Double endingAvailableBalance; // 期后可用余额
}