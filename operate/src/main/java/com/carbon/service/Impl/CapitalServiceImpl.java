package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.carbon.mapper.*;
import com.carbon.po.*;
import com.carbon.service.CapitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class CapitalServiceImpl implements CapitalService {
    @Autowired
    private CapitalAccountMapper capitalAccountMapper;
    @Autowired
    private CapitalTradeRecordMapper capitalTradeRecordMapper;
    @Autowired
    private DepositAndWithdrawalRecordMapper depositAndWithdrawalRecordMapper;
    @Autowired
    private DepositAndWithdrawalRequestRecordMapper depositAndWithdrawalRequestRecordMapper;
    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Override
    public void capitalIn(String fromAccountId, String toAccountId, Double amount) {
        // 1.获取账户
        BankAccount fromCapitalAccount = bankAccountMapper.selectById(fromAccountId);
        CapitalAccount toCapitalAccount = capitalAccountMapper.selectById(toAccountId);
        // 2.更改金额
        fromCapitalAccount.setCapital(fromCapitalAccount.getCapital() - amount);
        toCapitalAccount.setCapital(toCapitalAccount.getCapital() + amount);
        toCapitalAccount.setAvailableCapital(toCapitalAccount.getAvailableCapital() + amount);
        toCapitalAccount.setTransferCapital(toCapitalAccount.getTransferCapital() + amount);

        /*todo 可用可出资金的转换逻辑*/

        // 3.更新账户
        UpdateWrapper<BankAccount> fromCapitalAccountUpdateWrapper = new UpdateWrapper<>();
        fromCapitalAccountUpdateWrapper.eq("capital", fromCapitalAccount.getCapital());
        bankAccountMapper.update(fromCapitalAccount,fromCapitalAccountUpdateWrapper);
        UpdateWrapper<CapitalAccount> toCapitalAccountUpdateWrapper = new UpdateWrapper<>();
        toCapitalAccountUpdateWrapper.eq("capital", toCapitalAccount.getCapital());
        toCapitalAccountUpdateWrapper.eq("available_capital",toCapitalAccount.getAvailableCapital());
        toCapitalAccountUpdateWrapper.eq("transfer_capital",toCapitalAccount.getTransferCapital());
        capitalAccountMapper.update(toCapitalAccount,toCapitalAccountUpdateWrapper);
    }

    @Override
    public void capitalOut(String fromAccountId, String toAccountId, Double amount) {
        // 1.获取账户
        BankAccount fromCapitalAccount = bankAccountMapper.selectById(fromAccountId);
        CapitalAccount toCapitalAccount = capitalAccountMapper.selectById(toAccountId);
        // 2.更改金额
        fromCapitalAccount.setCapital(fromCapitalAccount.getCapital() + amount);
        toCapitalAccount.setCapital(toCapitalAccount.getCapital() - amount);
        toCapitalAccount.setAvailableCapital(toCapitalAccount.getAvailableCapital() - amount);
        toCapitalAccount.setTransferCapital(toCapitalAccount.getTransferCapital() - amount);

        /*todo 可用可出资金的转换逻辑*/

        // 3.更新账户
        UpdateWrapper<BankAccount> fromCapitalAccountUpdateWrapper = new UpdateWrapper<>();
        fromCapitalAccountUpdateWrapper.eq("capital", fromCapitalAccount.getCapital());
        bankAccountMapper.update(fromCapitalAccount,fromCapitalAccountUpdateWrapper);
        UpdateWrapper<CapitalAccount> toCapitalAccountUpdateWrapper = new UpdateWrapper<>();
        toCapitalAccountUpdateWrapper.eq("capital", toCapitalAccount.getCapital());
        toCapitalAccountUpdateWrapper.eq("available_capital",toCapitalAccount.getAvailableCapital());
        toCapitalAccountUpdateWrapper.eq("transfer_capital",toCapitalAccount.getTransferCapital());
        capitalAccountMapper.update(toCapitalAccount,toCapitalAccountUpdateWrapper);

    }

    @Override
    public CapitalAccount selectCapitalAccount(String accountId) {

        return capitalAccountMapper.selectById(accountId);
    }

    @Override
    public List<CapitalTradeRecord> selectCapitalTradeRecord(String operatorCode) {
        Map<String,Object> map=new HashMap<>();
        map.put("operator_code",operatorCode);
        return capitalTradeRecordMapper.selectByMap(map);
    }

    @Override
    public List<DepositAndWithdrawalRecord> selectDepositAndWithdrawalRecord(String operatorCode) {
        Map<String,Object> map=new HashMap<>();
        map.put("operator_code",operatorCode);
        return depositAndWithdrawalRecordMapper.selectByMap(map);
    }

    @Override
    public List<DepositAndWithdrawalRequestRecord> selectDepositAndWithdrawalRequestRecord(String operatorCode) {
        Map<String,Object> map=new HashMap<>();
        map.put("operator_code",operatorCode);
        return depositAndWithdrawalRequestRecordMapper.selectByMap(map);
    }
}
