package com.carbon.service.Impl;

import com.carbon.po.*;
import com.carbon.service.QuotaService;

import java.util.List;

public class QuotaServiceimpl implements QuotaService {


    @Override
    public List<ClientTradeQuota> SelectClientTradeQuota(String QuotaAccountId) {
        List<ClientTradeQuota> list =ClientTradeQuotaDao.getTradeQuota(QuotaAccountId);
        return list;
    }

    @Override
    public List<ClientRegisterQuota> SelectClientRegisterQuota(String ClientId) {
        List<ClientRegisterQuota> list =ClientRegisterQuotaDao.getRegisterQuota(ClientId);
        return list;
    }
    @Override
    public void QuotaIn(String QuotaAccountId, String subjectMatterCode, double amount) {
        // 1.获取登记配额与交易配额
        String clientid =QuotaAccountDao.getQuotaAccount(QuotaAccountID).getClientid();
        ClientTradeQuota clientTradeQuota = ClientTradeQuotaDao.getTradeQuota(QuotaAccountId,subjectMatterCode);
        ClientRegisterQuota clientRegisterQuota = ClientRegisterQuotaDao.getRegisterQuota(clientid,subjectMatterCode);
        // 2.更改交易账户配额数目
        clientTradeQuota.setAmount(clientTradeQuota.getAmount() + amount);
        clientRegisterQuota.setAmount(clientRegisterQuota.getAmount() - amount);
        /*todo 可用可出资金的转换逻辑*/

        // 3.更新账户
        ClientTradeQuotaDao.updateTradeQuota(clientTradeQuota);
        ClientRegisterQuotaDao.updateTradeQuota(clientTradeQuota);

    }

    @Override
    public void QuotaOut(String QuotaAccountId ,String subjectMatterCode,double amount) {
        // 1.获取登记配额与交易配额
        String clientid =QuotaAccountDao.getQuotaAccount(QuotaAccountID).getClientid();
        ClientTradeQuota clientTradeQuota = ClientTradeQuotaDao.getTradeQuota(QuotaAccountId,subjectMatterCode);
        ClientRegisterQuota clientRegisterQuota = ClientRegisterQuotaDao.getRegisterQuota(clientid,subjectMatterCode);
        // 2.更改交易账户配额数目
        clientTradeQuota.setAmount(clientTradeQuota.getAmount() - amount);
        clientRegisterQuota.setAmount(clientRegisterQuota.getAmount() + amount);
        /*todo 可用可出资金的转换逻辑*/

        // 3.更新账户
        ClientTradeQuotaDao.updateTradeQuota(clientTradeQuota);
        ClientRegisterQuotaDao.updateTradeQuota(clientTradeQuota);
    }

    @Override
    public List<QuotaTransferRecord> SelectTransferRecord(String QuotaAccountId) {
        List<QuotaTransferRecord> list = QuotaTransferRecordDao.getQuotaTransferRecord(QuotaAccountId);
        return list;
    }

    @Override
    public  List<QuotaTradeRecord> selectQuotaTradeRecord(String QuotaAccountId) {
        List<QuotaTradeRecord> list = QuotaTradeRecordDao.getQuotaTradeRecord(QuotaAccountId);
        return list;
    }
}
