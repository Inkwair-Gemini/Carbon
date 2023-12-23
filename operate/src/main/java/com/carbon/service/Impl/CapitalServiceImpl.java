package com.carbon.service.Impl;

import com.carbon.dao.CapitalDao;
import com.carbon.po.CapitalAccount;
import com.carbon.po.CapitalTradeRecord;
import com.carbon.po.DepositAndWithdrawalRecord;
import com.carbon.po.DepositAndWithdrawalRequestRecord;
import com.carbon.service.CapitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CapitalServiceImpl implements CapitalService {
    @Autowired
    CapitalDao capitalDao;

    @Override
    public void capitalIn(String fromBankId, String toAccountId, Double amount) {
        // 1.获取账户
        BankAccount bankAccount = bankAccountDao.getBankAccount(fromBankId);
        CapitalAccount capitalAccount = capitalDao.getCapitalAccount(toAccountId);
        // 2.更改金额
        bankAccount.setAmount(bankAccount.getAmount() - amount);
        capitalAccount.setCapital(capitalAccount.getCapital() + amount);
        /*todo 可用可出资金的转换逻辑*/

        // 3.更新账户
        bankDao.updateBankAccount(bankAccount);
        capitalDao.updateCapitalAccount(capitalAccount);
    }

    @Override
    public void capitalOut(String fromAccountId, String toBankId, Double amount) {
        // 1.获取账户
        BankAccount bankAccount = bankAccountDao.getBankAccount(toBankId);
        CapitalAccount capitalAccount = capitalDao.getCapitalAccount(toBankId);
        // 2.更改金额
        bankAccount.setAmount(bankAccount.getAmount() + amount);
        capitalAccount.setCapital(capitalAccount.getCapital() - amount);
        /*todo 可用可出资金的转换逻辑*/

        // 3.更新账户
        bankDao.updateBankAccount(bankAccount);
        capitalDao.updateCapitalAccount(capitalAccount);
    }

    @Override
    public CapitalAccount selectCapitalAccount(String accountId) {
        return capitalDao.selectCapitalAccount(accountId);
    }

    @Override
    public List<CapitalTradeRecord> selectCapitalTradeRecord(String operatorCode) {
        return capitalDao.selectCapitalTradeRecord(operatorCode);
    }

    @Override
    public List<DepositAndWithdrawalRecord> selectDepositAndWithdrawalRecord(String operatorCode) {
        return capitalDao.selectDepositAndWithdrawalRecord(operatorCode);
    }

    @Override
    public List<DepositAndWithdrawalRequestRecord> selectDepositAndWithdrawalRequestRecord(String operatorCode) {
        return capitalDao.selectDepositAndWithdrawalRequestRecord(operatorCode);
    }
}
