package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.carbon.mapper.*;
import com.carbon.po.*;
import com.carbon.service.QuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class QuotaServiceImpl implements QuotaService {

    @Autowired
    private ClientTradeQuotaMapper clientTradeQuotaMapper;
    @Autowired
    private ClientRegisterQuotaMapper clientRegisterQuotaMapper;
    @Autowired
    private QuotaAccountMapper quotaAccountMapper;
    @Autowired
    private QuotaTransferRecordMapper quotaTransferRecordMapper;
    @Autowired
    private QuotaTradeRecordMapper quotaTradeRecordMapper;

    @Override
    public List<ClientTradeQuota> SelectClientTradeQuota(String QuotaAccountId, String subjectMatterCode) {
        Map<String,Object> map=new HashMap<>();
        map.put("quota_account_id",QuotaAccountId);
        List<ClientTradeQuota> list = clientTradeQuotaMapper.selectByMap(map);
        return list;
    }

    @Override
    public List<ClientRegisterQuota> SelectClientRegisterQuota(String clientId, String subjectMatterCode) {
        Map<String,Object> map=new HashMap<>();
        map.put("client_id",clientId);
        List<ClientRegisterQuota> list = clientRegisterQuotaMapper.selectByMap(map);
        return list;
    }

    @Override
    public void QuotaIn(String accountId, String subjectMatterCode, Double amount) {
        // 1.获取登记配额与交易配额
        String clientId =quotaAccountMapper.selectById(accountId).getClientId();
        Map<String,Object> clientRegisterQuotamap=new HashMap<>();
        clientRegisterQuotamap.put("client_id",clientId);
        clientRegisterQuotamap.put("subject_matter_code",subjectMatterCode);
        List<ClientRegisterQuota> clientRegisterQuota = clientRegisterQuotaMapper.selectByMap(clientRegisterQuotamap);

        Map<String,Object> clientTradeQuotamap=new HashMap<>();
        clientTradeQuotamap.put("quota_quota_account_id",accountId);
        clientTradeQuotamap.put("subject_matter_code",subjectMatterCode);
        List<ClientTradeQuota> clientTradeQuota = clientTradeQuotaMapper.selectByMap(clientTradeQuotamap);

        // 2.更改交易账户配额数目
        clientTradeQuota.get(0).setAmount(clientTradeQuota.get(0).getAmount() + amount);
        clientRegisterQuota.get(0).setAmount(clientRegisterQuota.get(0).getAmount() - amount);
        /*todo 可用可出资金的转换逻辑*/

        // 3.更新账户
        UpdateWrapper<ClientRegisterQuota> clientRegisterQuotaUpdateWrapper = new UpdateWrapper<>();
        clientRegisterQuotaUpdateWrapper.eq("client_id", clientId);
        clientRegisterQuotaUpdateWrapper.eq("subject_matter_code",subjectMatterCode);
        clientRegisterQuotaMapper.update(clientRegisterQuota.get(0), clientRegisterQuotaUpdateWrapper);

        UpdateWrapper<ClientTradeQuota> clientTradeQuotaUpdateWrapper = new UpdateWrapper<>();
        clientTradeQuotaUpdateWrapper.eq("quota_account_id", accountId);
        clientTradeQuotaUpdateWrapper.eq("subject_matter_code",subjectMatterCode);
        clientTradeQuotaMapper.update(clientTradeQuota.get(0), clientTradeQuotaUpdateWrapper);

    }

    @Override
    public void QuotaOut(String accountId ,String subjectMatterCode,Double amount) {
        // 1.获取登记配额与交易配额
        String clientId =quotaAccountMapper.selectById(accountId).getClientId();
        Map<String,Object> clientRegisterQuotamap=new HashMap<>();
        clientRegisterQuotamap.put("client_id",clientId);
        clientRegisterQuotamap.put("subject_matter_code",subjectMatterCode);
        List<ClientRegisterQuota> clientRegisterQuota = clientRegisterQuotaMapper.selectByMap(clientRegisterQuotamap);

        Map<String,Object> clientTradeQuotamap=new HashMap<>();
        clientTradeQuotamap.put("quota_account_id",accountId);
        clientTradeQuotamap.put("subject_matter_code",subjectMatterCode);
        List<ClientTradeQuota> clientTradeQuota = clientTradeQuotaMapper.selectByMap(clientTradeQuotamap);

        // 2.更改交易账户配额数目
        clientTradeQuota.get(0).setAmount(clientTradeQuota.get(0).getAmount() - amount);
        clientRegisterQuota.get(0).setAmount(clientRegisterQuota.get(0).getAmount() + amount);
        /*todo 可用可出资金的转换逻辑*/

        // 3.更新账户
        UpdateWrapper<ClientRegisterQuota> clientRegisterQuotaUpdateWrapper = new UpdateWrapper<>();
        clientRegisterQuotaUpdateWrapper.eq("client_id", clientId);
        clientRegisterQuotaUpdateWrapper.eq("subject_matter_code",subjectMatterCode);
        clientRegisterQuotaMapper.update(clientRegisterQuota.get(0), clientRegisterQuotaUpdateWrapper);

        UpdateWrapper<ClientTradeQuota> clientTradeQuotaUpdateWrapper = new UpdateWrapper<>();
        clientTradeQuotaUpdateWrapper.eq("quota_account_id", accountId);
        clientTradeQuotaUpdateWrapper.eq("subject_matter_code",subjectMatterCode);
        clientTradeQuotaMapper.update(clientTradeQuota.get(0), clientTradeQuotaUpdateWrapper);
    }

    @Override
    public List<QuotaTransferRecord> SelectTransferRecord(String clientId) {
        Map<String,Object> map=new HashMap<>();
        map.put("client_id",clientId);
        List<QuotaTransferRecord> list = quotaTransferRecordMapper.selectByMap(map);
        return list;
    }

    @Override
    public  List<QuotaTradeRecord> selectQuotaTradeRecord(String clientId) {
        Map<String,Object> map=new HashMap<>();
        map.put("client_id",clientId);
        List<QuotaTradeRecord> list = quotaTradeRecordMapper.selectByMap(map);
        return list;
    }
}
