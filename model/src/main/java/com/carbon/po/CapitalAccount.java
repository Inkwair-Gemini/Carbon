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
    private String bindBankAccount;
}
