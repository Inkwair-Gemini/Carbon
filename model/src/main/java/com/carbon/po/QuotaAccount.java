package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 配额账户
public class QuotaAccount {
    private String id;
    private String clientId;
    private String password;
    private String accountType; // 账户类型
    private List<ClientTradeQuota> clientTradeQuotas; // 客户交易配额
}