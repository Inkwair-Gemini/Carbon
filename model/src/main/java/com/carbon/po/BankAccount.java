package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    //银行账户
    private String id;
    //银行名称
    private String bank;
    //资金余额
    private Double capital;
}
