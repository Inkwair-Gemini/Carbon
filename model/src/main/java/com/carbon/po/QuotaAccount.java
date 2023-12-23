package com.carbon.po;

import com.carbon.input.TradeQuota;

// 配额账户
public class QuotaAccount {
    private String id;
    private String clientId;
    private String password;
    private String accountType; // 账户类型
    private List<TradeQuota> tradeQuotas;
}