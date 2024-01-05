package com.carbon.utils;

import com.carbon.mapper.CapitalAccountMapper;
import com.carbon.mapper.ClientMapper;
import com.carbon.mapper.ClientOperatorMapper;
import com.carbon.mapper.QuotaAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//使用前先注入ClientIdUtils
@Component
public class ClientIdUtils {
    private static CapitalAccountMapper capitalAccountMapper;
    private static QuotaAccountMapper quotaAccountMapper;
    private static ClientOperatorMapper clientOperatorMapper;
    private static ClientMapper clientMapper;

    public String getIdByCapitalAccountId(String accountId){
         return capitalAccountMapper.selectById(accountId).getClientId();
    }
    public String getIdByQuotaAccountId(String accountId){
        return quotaAccountMapper.selectById(accountId).getClientId();
    }
    public String getIdByOperatorCode(String operatorCode){
        return clientOperatorMapper.selectById(operatorCode).getClientId();
    }
    public String getNameByCapitalAccountId(String accountId){
        String clientId = capitalAccountMapper.selectById(accountId).getClientId();
        return clientMapper.selectById(clientId).getName();
    }
    public String getNameByQuotaAccountId(String accountId){
        String clientId = quotaAccountMapper.selectById(accountId).getClientId();
        return clientMapper.selectById(clientId).getName();
    }
    public String getNameByOperatorCode(String operatorCode){
        String clientId = clientOperatorMapper.selectById(operatorCode).getClientId();
        return clientMapper.selectById(clientId).getName();
    }
}
