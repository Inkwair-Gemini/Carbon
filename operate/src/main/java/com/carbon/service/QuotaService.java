package com.carbon.service;

import com.carbon.po.ClientRegisterQuota;
import com.carbon.po.ClientTradeQuota;
import com.carbon.po.QuotaTradeRecord;
import com.carbon.po.QuotaTransferRecord;

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
}
