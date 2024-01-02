package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 配额账户
public class QuotaAccount implements Serializable {
    private String id;
    private String clientId;
    private String password;
    private String accountType; // 账户类型
    private List<ClientTradeQuota> clientTradeQuotas; // 客户交易配额

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<ClientTradeQuota> getClientTradeQuotas() {
        return clientTradeQuotas;
    }

    public void setClientTradeQuotas(List<ClientTradeQuota> clientTradeQuotas) {
        this.clientTradeQuotas = clientTradeQuotas;
    }
}