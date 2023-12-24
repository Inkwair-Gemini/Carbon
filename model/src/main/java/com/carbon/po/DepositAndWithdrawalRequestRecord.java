package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 出入金申请记录
public class DepositAndWithdrawalRequestRecord {
    private String id;
    private Date time;
    private String initiator; // 发起员
    private String bindBank; // 银行Id
    private String bankAccountId; // 银行账户Id
    private String type; // 出/入
    private String requestState; // 申请状态
    private Double actualAmount; // 发生金额
    private String description; // 备注
}