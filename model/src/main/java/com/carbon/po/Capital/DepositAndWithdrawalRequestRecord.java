package com.carbon.po.Capital;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 出入金申请记录
public class DepositAndWithdrawalRequestRecord implements Serializable {
    private String id;
    private Timestamp time;
    private String account; // 资金账号
    private String bindBank; // 银行
    private String bankAccountId; // 银行账户Id
    private String type; // 出/入
    private String requestState; // 申请状态 成功/失败
    private Double actualAmount; // 发生金额
    private String description; // 备注
}