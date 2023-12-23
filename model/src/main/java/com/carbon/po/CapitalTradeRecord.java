package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 资金交易流水
public class CapitalTradeRecord {
    private String id;
    private String date;
    private String time;
    private String operatorCode; // 操作员代码
    private String capitalAccount; // 资金账号
    private String type; // 划转类型
    private double actualAmount; // 发生金额
    private double endingBalance; // 期后余额
    private double endingAvailableBalance; // 期后可用余额
}