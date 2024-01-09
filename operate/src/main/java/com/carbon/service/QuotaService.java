package com.carbon.service;

import com.carbon.po.Quota.ClientRegisterQuota;
import com.carbon.po.Quota.ClientTradeQuota;
import com.carbon.po.Quota.QuotaTradeRecord;
import com.carbon.po.Quota.QuotaTransferRecord;

import java.util.List;

public interface QuotaService {
    //查询交易配额
    List<ClientTradeQuota> SelectClientTradeQuota(String QuotaAccountId, String subjectMatterCode);
    //查询登记配额
    List<ClientRegisterQuota> SelectClientRegisterQuota(String ClientId, String subjectMatterCode);
    //转入交易配额
    void QuotaIn(String QuotaAccountId ,String subjectMatterCode,Double amount);
    //转出交易配额
    void QuotaOut(String QuotaAccountId ,String subjectMatterCode,Double amount);
    //查询转入转出流水
    List<QuotaTransferRecord> SelectTransferRecord(String clientId);
    //查询配额交易流水
    List<QuotaTradeRecord> selectQuotaTradeRecord(String clientId);
    //新增转入转出流水
    void addQuotaTransferRecord(QuotaTransferRecord quotaTransferRecord);
    //新增配额交易流水
    void addQuotaTradeRecord(QuotaTradeRecord quotaTradeRecord);
    //配额转让
    void quotaTransfer(String listingQuotaAccountId,String delistingQuotaAccountId,Double amount,String subjectMatterCode,String flowType);
}
