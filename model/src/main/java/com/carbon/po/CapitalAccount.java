package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
//资金账户
public class CapitalAccount implements Serializable {
    private String id;
    private String password;
    //账户类型
    private String accountType;
    //资金余额
    private Double capital;
    //可用资金
    private Double availableCapital;
    //可出资金
    private Double transferCapital;
    //银行Id
    private String bindBank;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getCapital() {
        return capital;
    }

    public void setCapital(Double capital) {
        this.capital = capital;
    }

    public Double getAvailableCapital() {
        return availableCapital;
    }

    public void setAvailableCapital(Double availableCapital) {
        this.availableCapital = availableCapital;
    }

    public Double getTransferCapital() {
        return transferCapital;
    }

    public void setTransferCapital(Double transferCapital) {
        this.transferCapital = transferCapital;
    }

    public String getBindBank() {
        return bindBank;
    }

    public void setBindBank(String bindBank) {
        this.bindBank = bindBank;
    }
}
