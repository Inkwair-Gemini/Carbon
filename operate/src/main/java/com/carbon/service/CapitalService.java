package com.carbon.service;

import com.carbon.po.CapitalAccount;
import com.carbon.po.CapitalTradeRecord;

public interface CapitalService {
    //转入交易资金
    Boolean capitalIn(String fromBankId,String toAccountId,Double amount);
    //转出交易资金
    Boolean capitalOut(String fromAccountId,String toBankId,Double value);
    //查询资金账户
    CapitalAccount selectCapitalAccount(String accountId);
    //查询资金交易流水
    CapitalTradeRecord selectCapitalTradeRecord(String accountId);
    //查询出入金记录
    DepositAndWithdrawalRecord selectDepositAndWithdrawalRecord();
    //查询出入金申请记录
    DepositAndWithdrawalRequestRecord selectDepositAndWithdrawalRequestRecord();
}
