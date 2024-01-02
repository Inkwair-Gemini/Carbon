package com.carbon.service;

import com.carbon.po.CapitalAccount;
import com.carbon.po.CapitalTradeRecord;
import com.carbon.po.DepositAndWithdrawalRecord;
import com.carbon.po.DepositAndWithdrawalRequestRecord;

import java.util.List;

public interface CapitalService {
    //转入交易资金
    void capitalIn(String fromAccountId,String toAccountId,Double amount);
    //转出交易资金
    void capitalOut(String fromAccountId,String toAccountId,Double value);
    //查询资金账户
    CapitalAccount selectCapitalAccount(String accountId);
    //查询资金交易流水
    List<CapitalTradeRecord> selectCapitalTradeRecord(String operatorCode);
    //查询出入金记录
    List<DepositAndWithdrawalRecord> selectDepositAndWithdrawalRecord(String operatorCode);
    //查询出入金申请记录
    List<DepositAndWithdrawalRequestRecord> selectDepositAndWithdrawalRequestRecord(String operatorCode);
}
