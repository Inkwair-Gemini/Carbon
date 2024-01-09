package com.carbon.service;

import com.carbon.input.CapitalAndQuota.CapitalTransferPost;
import com.carbon.po.Capital.CapitalAccount;
import com.carbon.po.Capital.CapitalTradeRecord;
import com.carbon.po.Capital.DepositAndWithdrawalRecord;
import com.carbon.po.Capital.DepositAndWithdrawalRequestRecord;

import java.util.List;

public interface CapitalService {
    //出入金申请记录
    void DepositAndWithdrawalRequestRecord(CapitalTransferPost capitalTransferPost);
    //出入金记录
    void DepositAndWithdrawalRecord(CapitalTransferPost capitalTransferPost,String operatorCode);
    //转入交易资金
    void capitalIn(String capitalAccountId,Double amount);
    //转出交易资金
    void capitalOut(String capitalAccountId,Double amount);
    //查询资金账户
    CapitalAccount selectCapitalAccount(String accountId);
    //查询资金交易流水
    List<CapitalTradeRecord> selectCapitalTradeRecord(String ClientId);
    //查询出入金记录
    List<DepositAndWithdrawalRecord> selectDepositAndWithdrawalRecord(String ClientId);
    //查询出入金申请记录
    List<DepositAndWithdrawalRequestRecord> selectDepositAndWithdrawalRequestRecord(String ClientId);
    //资金转让
    void capitalTransfer(String listingCapitalAccountId,String delistingCapitalAccountId,Double amount,String flowType);
}
