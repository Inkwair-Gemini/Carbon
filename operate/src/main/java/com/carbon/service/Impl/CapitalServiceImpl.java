package com.carbon.service.Impl;

import com.carbon.input.CapitalAndQuota.CapitalTransferPost;
import com.carbon.mapper.*;
import com.carbon.po.*;
import com.carbon.po.Capital.*;
import com.carbon.po.ClientOperator;
import com.carbon.service.CapitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

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
    @Autowired
    private CapitalTransferPostMapper capitalTransferPostMapper;
    @Autowired
    private ClientOperatorMapper clientOperatorMapper;

    @Override
    public void DepositAndWithdrawalRequestRecord(CapitalTransferPost capitalTransferPost){
        //1.创建转入转出资金申请记录类来存储信息
        DepositAndWithdrawalRequestRecord depositAndWithdrawalRequestRecord = new DepositAndWithdrawalRequestRecord();

        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
        depositAndWithdrawalRequestRecord.setTime(currentTimestamp);//时间
        String capitalAccountId=capitalTransferPost.getCapitalAccount();
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(capitalAccountId);
        String bankAccountId = capitalAccount.getBindBankAccount();
        BankAccount bankAccount= bankAccountMapper.selectById(bankAccountId);
        depositAndWithdrawalRequestRecord.setBindBank(bankAccount.getBank());//绑定银行（名称）
        depositAndWithdrawalRequestRecord.setBankAccountId(bankAccountId);//银行id
        depositAndWithdrawalRequestRecord.setType(capitalTransferPost.getType());//类别
        depositAndWithdrawalRequestRecord.setActualAmount(capitalTransferPost.getActualAmount());//发生金额

        //2.申请状态的判定

        if(capitalTransferPost.getType()=="转入"){
            if(bankAccount.getCapital()>= capitalTransferPost.getActualAmount()){
                depositAndWithdrawalRequestRecord.setRequestState("成功");
                depositAndWithdrawalRequestRecord.setDescription("无");
            }else{
                depositAndWithdrawalRequestRecord.setRequestState("失败");
                depositAndWithdrawalRequestRecord.setDescription("银行余额不足");
            }
        }
        if(capitalTransferPost.getType()=="转出"){
            if(capitalAccount.getAvailableCapital()>= capitalTransferPost.getActualAmount()){
                depositAndWithdrawalRequestRecord.setRequestState("成功");
                depositAndWithdrawalRequestRecord.setDescription("无");
            }else{
                depositAndWithdrawalRequestRecord.setRequestState("失败");
                depositAndWithdrawalRequestRecord.setDescription("账户余额不足");
            }
        }

        //3.更新出入金申请表
        depositAndWithdrawalRequestRecordMapper.insert(depositAndWithdrawalRequestRecord);

    };

    @Override
    public void DepositAndWithdrawalRecord(CapitalTransferPost capitalTransferPost,String operatorCode){
        //1.创建转入转出资金记录类来存储信息
        DepositAndWithdrawalRecord depositAndWithdrawalRecord = new DepositAndWithdrawalRecord();

        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
        depositAndWithdrawalRecord.setTime(currentTimestamp);//时间
        depositAndWithdrawalRecord.setOperatorCode(operatorCode);//发起员
        String capitalAccountId=capitalTransferPost.getCapitalAccount();
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(capitalAccountId);
        depositAndWithdrawalRecord.setCapitalAccount(capitalAccountId);
        depositAndWithdrawalRecord.setType(capitalTransferPost.getType());//类别
        depositAndWithdrawalRecord.setActualAmount(capitalTransferPost.getActualAmount());//发生金额
        depositAndWithdrawalRecord.setEndingBalance(capitalAccount.getCapital());//期后余额
        depositAndWithdrawalRecord.setEndingAvailableBalance(capitalAccount.getAvailableCapital());//期后可用余额

        //3.更新出入金申请表
        depositAndWithdrawalRecordMapper.insert(depositAndWithdrawalRecord);

    };
    @Override
    public void capitalIn(String capitalAccountId, Double amount) {
        // 1.获取账户
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(capitalAccountId);
        String bankAccountId = capitalAccount.getBindBankAccount();
        BankAccount bankAccount= bankAccountMapper.selectById(bankAccountId);
        // 2.更改金额
        bankAccount.setCapital(bankAccount.getCapital() - amount);
        capitalAccount.setCapital(capitalAccount.getCapital() + amount);
        capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() + amount);

        /*todo 可用可出资金的转换逻辑*/

        // 3.更新账户
        bankAccountMapper.updateById(bankAccount);
        capitalAccountMapper.updateById(capitalAccount);
    }

    @Override
    public void capitalOut(String capitalAccountId, Double amount) {
        // 1.获取账户
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(capitalAccountId);
        String bankAccountId = capitalAccount.getBindBankAccount();
        BankAccount bankAccount= bankAccountMapper.selectById(bankAccountId);

        // 2.更改金额
        bankAccount.setCapital(bankAccount.getCapital() + amount);
        capitalAccount.setCapital(capitalAccount.getCapital() - amount);
        capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - amount);

        // 3.更新账户
        bankAccountMapper.updateById(bankAccount);
        capitalAccountMapper.updateById(capitalAccount);

    }

    @Override
    public CapitalAccount selectCapitalAccount(String accountId) {
        return capitalAccountMapper.selectById(accountId);
    }

    @Override
    public List<CapitalTradeRecord> selectCapitalTradeRecord(String clientId) {
        //1.获取客户操作员
        Map<String,Object> clientOperatormap=new HashMap<>();
        clientOperatormap.put("client_id",clientId);
        List<ClientOperator> clientOperators = clientOperatorMapper.selectByMap(clientOperatormap);
        List<CapitalTradeRecord> capitalTradeRecords = new ArrayList<>();
        //2.获取所有操作员的记录
        for(int i=0;i<clientOperators.size();i++){
            Map<String,Object> map=new HashMap<>();
            map.put("operator_code",clientOperators.get(i).getId());
            capitalTradeRecords.addAll(capitalTradeRecordMapper.selectByMap(map));
        }
        return capitalTradeRecords;
    }

    @Override
    public List<DepositAndWithdrawalRecord> selectDepositAndWithdrawalRecord(String clientId) {
        //1.获取客户操作员
        Map<String,Object> clientOperatormap=new HashMap<>();
        clientOperatormap.put("client_id",clientId);
        List<ClientOperator> clientOperators = clientOperatorMapper.selectByMap(clientOperatormap);
        //2.获取所有操作员的记录
        List<DepositAndWithdrawalRecord> depositAndWithdrawalRecords = new ArrayList<>();
        for(int i=0;i<clientOperators.size();i++){
            Map<String,Object> map=new HashMap<>();
            map.put("operator_code",clientOperators.get(i).getId());
            depositAndWithdrawalRecords.addAll(depositAndWithdrawalRecordMapper.selectByMap(map));
        }
        return depositAndWithdrawalRecords;
    }

    @Override
    public List<DepositAndWithdrawalRequestRecord> selectDepositAndWithdrawalRequestRecord(String clientId) {
        //1.获取客户操作员
        Map<String,Object> clientOperatormap=new HashMap<>();
        clientOperatormap.put("client_id",clientId);
        List<ClientOperator> clientOperators = clientOperatorMapper.selectByMap(clientOperatormap);
        List<DepositAndWithdrawalRequestRecord> depositAndWithdrawalRequestRecords = new ArrayList<>();
        //2.获取所有操作员记录
        for(int i=0;i<clientOperators.size();i++){
            Map<String,Object> map=new HashMap<>();
            map.put("operator_code",clientOperators.get(i).getId());
            depositAndWithdrawalRequestRecords.addAll(depositAndWithdrawalRequestRecordMapper.selectByMap(map));
        }
        return depositAndWithdrawalRequestRecords;
    }
    @Override
    public void capitalTransfer(String listingCapitalAccountId,String delistingCapitalAccountId ,Double amount,String flowType){
        //1.获取账户
        CapitalAccount listingCapitalAccount = capitalAccountMapper.selectById(listingCapitalAccountId);
        CapitalAccount delistingCapitalAccount = capitalAccountMapper.selectById(delistingCapitalAccountId);

        //2.修改账户金额
        if(flowType.equals("买入")){
            listingCapitalAccount.setCapital(listingCapitalAccount.getCapital() - amount);
            listingCapitalAccount.setUnavailableCapital(listingCapitalAccount.getUnavailableCapital() - amount);
            delistingCapitalAccount.setCapital(delistingCapitalAccount.getCapital() + amount);
            delistingCapitalAccount.setAvailableCapital(delistingCapitalAccount.getAvailableCapital() + amount);
        }else{
            listingCapitalAccount.setCapital(listingCapitalAccount.getCapital() + amount);
            listingCapitalAccount.setUnavailableCapital(listingCapitalAccount.getUnavailableCapital() + amount);
            delistingCapitalAccount.setCapital(delistingCapitalAccount.getCapital() - amount);
            delistingCapitalAccount.setAvailableCapital(delistingCapitalAccount.getAvailableCapital() - amount);
        }


        //3.更新账户
        capitalAccountMapper.updateById(listingCapitalAccount);
        capitalAccountMapper.updateById(delistingCapitalAccount);
    };
}
