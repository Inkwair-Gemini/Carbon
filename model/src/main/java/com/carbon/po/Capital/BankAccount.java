package com.carbon.po.Capital;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount implements Serializable {
    //银行账户
    private String id;
    //银行名称
    private String bank;
    //资金余额
    private Double capital;
    //支付密码
    private String password;
    //预留手机号
    private String phoneNumber;
}
