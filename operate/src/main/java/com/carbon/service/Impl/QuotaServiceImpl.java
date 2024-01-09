package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.carbon.Utils.StringUtils;
import com.carbon.mapper.*;
import com.carbon.po.*;
import com.carbon.po.Quota.ClientRegisterQuota;
import com.carbon.po.Quota.ClientTradeQuota;
import com.carbon.po.Quota.QuotaTradeRecord;
import com.carbon.po.Quota.QuotaTransferRecord;
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
        if(StringUtils.isNotEmpty(QuotaAccountId)){
            map.put("quota_account_id",QuotaAccountId);
        }
        if(StringUtils.isNotEmpty(subjectMatterCode)){
            map.put("subject_matter_code",subjectMatterCode);
        }
        return  clientTradeQuotaMapper.selectByMap(map);
    }

    @Override
    public List<ClientRegisterQuota> SelectClientRegisterQuota(String clientId, String subjectMatterCode) {
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isNotEmpty(clientId)){
            map.put("client_id",clientId);
        }
        if(StringUtils.isNotEmpty(subjectMatterCode)){
            map.put("subject_matter_code",subjectMatterCode);
        }
        return clientRegisterQuotaMapper.selectByMap(map);
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
        clientTradeQuotamap.put("quota_account_id",accountId);
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
    @Override
    public void addQuotaTransferRecord(QuotaTransferRecord quotaTransferRecord){
        quotaTransferRecordMapper.insert(quotaTransferRecord);
    };
    @Override
    public void addQuotaTradeRecord(QuotaTradeRecord quotaTradeRecord){
        quotaTradeRecordMapper.insert(quotaTradeRecord);
    };
    @Override
    public void quotaTransfer(String listingQuotaAccountId,String delistingQuotaAccountId,Double amount,String subjectMatterCode,String flowType){
        //1.获取账户有关标的物的交易配额
        Map<String,Object> listingClientTradeQuotamap=new HashMap<>();
        listingClientTradeQuotamap.put("quota_account_id",listingQuotaAccountId);
        listingClientTradeQuotamap.put("subject_matter_code",subjectMatterCode);
        List<ClientTradeQuota> listingClientTradeQuota = clientTradeQuotaMapper.selectByMap(listingClientTradeQuotamap);

        Map<String,Object> delistingClientTradeQuotamap=new HashMap<>();
        delistingClientTradeQuotamap.put("quota_account_id",delistingQuotaAccountId);
        delistingClientTradeQuotamap.put("subject_matter_code",subjectMatterCode);
        List<ClientTradeQuota> delistingClientTradeQuota = clientTradeQuotaMapper.selectByMap(delistingClientTradeQuotamap);

        //2.修改账户配额
        if(flowType.equals("卖出")){
            //卖出挂牌
            listingClientTradeQuota.get(0).setAmount(listingClientTradeQuota.get(0).getAmount() - amount);
            delistingClientTradeQuota.get(0).setAmount(delistingClientTradeQuota.get(0).getAmount() + amount);
        } else{
            //买入挂牌
            listingClientTradeQuota.get(0).setAmount(listingClientTradeQuota.get(0).getAmount() + amount);
            delistingClientTradeQuota.get(0).setAmount(delistingClientTradeQuota.get(0).getAmount() - amount);
        }


        //3.更新账户
        UpdateWrapper<ClientTradeQuota> listingClientTradeQuotaUpdateWrapper = new UpdateWrapper<>();
        listingClientTradeQuotaUpdateWrapper.eq("quota_account_id", listingQuotaAccountId);
        listingClientTradeQuotaUpdateWrapper.eq("subject_matter_code",subjectMatterCode);
        clientTradeQuotaMapper.update(listingClientTradeQuota.get(0), listingClientTradeQuotaUpdateWrapper);

        UpdateWrapper<ClientTradeQuota> delistingClientTradeQuotaUpdateWrapper = new UpdateWrapper<>();
        delistingClientTradeQuotaUpdateWrapper.eq("quota_account_id", delistingQuotaAccountId);
        delistingClientTradeQuotaUpdateWrapper.eq("subject_matter_code",subjectMatterCode);
        clientTradeQuotaMapper.update(delistingClientTradeQuota.get(0), delistingClientTradeQuotaUpdateWrapper);

    };
}
