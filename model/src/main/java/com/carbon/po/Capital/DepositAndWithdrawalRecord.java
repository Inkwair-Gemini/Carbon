package com.carbon.po.Capital;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
// 出入金流水
public class DepositAndWithdrawalRecord implements Serializable {
    private String id;
    private Timestamp time;
    private String capitalAccount; // 资金账户Id
    private String operatorCode;
    @TableField("`type`")
    private String type; // 出/入
    private Double actualAmount;
    private Double endingBalance;
    private Double endingAvailableBalance;
}