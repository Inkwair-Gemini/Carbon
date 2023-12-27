package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
// 出入金流水
public class DepositAndWithdrawalRecord {
    private String id;
    private Timestamp time;
    private String capitalAccount; // 资金账户Id
    private String operatorCode;
    private String type; // 出/入
    private Double actualAmount;
    private Double endingBalance;
    private Double endingAvailableBalance;
}