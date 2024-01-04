package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.input.AuctionPost;
import com.carbon.input.AuctionRequest;
import com.carbon.input.ListingPost;
import com.carbon.mapper.*;
import com.carbon.po.*;
import com.carbon.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @projectName: Carbon
 * @package: com.carbon.service.Impl
 * @className: AuctionServiceImpl
 * @author: Doctor.H
 * @description: 单向竞价业务实现
 * @date: 2023/12/27 19:44
 */
@Service
public class AuctionServiceImpl implements AuctionService {
    @Autowired
    ClientOperatorMapper clientOperatorMapper;
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    CapitalAccountMapper capitalAccountMapper;
    @Autowired
    QuotaAccountMapper quotaAccountMapper;
    @Autowired
    ClientTradeQuotaMapper clientTradeQuotaMapper;
    @Autowired
    AuctionRequestMapper auctionRequestMapper;
    @Autowired
    AuctionQuotaMapper auctionQuotaMapper;
    @Autowired
    AuctionPostMapper auctionPostMapper;
    @Autowired
    AuctionDoneRecordMapper auctionDoneRecordMapper;

    @Override
    public void requestAuction(AuctionRequest auctionRequest) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(auctionRequest.getOperatorCode());
        Client client=clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        QuotaAccount quotaAccount = quotaAccountMapper.selectById(client.getQuotaAccountId());

        QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_id", quotaAccount.getId())
                .eq("subject_matter_code", auctionRequest.getSubjectMatterCode());
        ClientTradeQuota clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
        //todo 1.判断是否有足够的配额
        boolean isEnoughQuota = clientTradeQuota.getAvailableQuotaAmount() >= auctionRequest.getAmount();
        //todo 2.判断是否有足够的资金
        //boolean isEnoughCapital = capitalAccount.getCapital() >= auctionRequest.getCapital();
        //todo 3.提交申请
        if (isEnoughQuota/* && isEnoughCapital*/) {
            //todo 3.1.冻结部分配额
            QueryWrapper<ClientTradeQuota> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq("account_id", quotaAccount.getId())
                    .eq("subject_matter_code", auctionRequest.getSubjectMatterCode());
            clientTradeQuota.setAvailableQuotaAmount(clientTradeQuota.getAvailableQuotaAmount() - auctionRequest.getAmount());
            clientTradeQuota.setUnavailableQuotaAmount(clientTradeQuota.getUnavailableQuotaAmount() + auctionRequest.getAmount());
            //todo 3.2.冻结部分资金
            //capitalAccount.setCapital(capitalAccount.getCapital() - auctionRequest.getCapital());
            //todo 3.3.更新配额账户
            clientTradeQuotaMapper.update(clientTradeQuota,updateWrapper);
            //todo 3.4.更新资金账户
            //capitalDao.updateCapitalAccount(capitalAccount);
            //todo 3.6.添加请求记录
            auctionRequestMapper.insert(auctionRequest);
            //todo 3.7.等待申请时间

            //todo 3.8.新建单向竞价商品
            AuctionQuota auctionQuota=new AuctionQuota();
            //todo 3.8.1.添加auctionRequest内信息到auctionQuota
            auctionQuota.setClientId(clientOperator.getClientId());
            auctionQuota.setSubjectMatterCode(auctionRequest.getSubjectMatterCode());
            auctionQuota.setSubjectMatterName(auctionRequest.getSubjectMatterName());
            auctionQuota.setPrice(auctionRequest.getPrice());
            auctionQuota.setAmount(auctionRequest.getAmount());
            auctionQuota.setTotalBalance(Double.toString(auctionRequest.getPrice()*auctionRequest.getAmount()));
            //todo 3.8.2 添加未来发布时间、状态
            //获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, 2); // 当前时间加两个小时

            auctionQuota.setTime(new Timestamp(calendar.getTimeInMillis()));//添加未来平台发布时间
            auctionQuota.setStatus("未成交");
            //todo 3.9 向auctionQuota插入单向竞价商品
            auctionQuotaMapper.insert(auctionQuota);

        }
    }

    @Override
    public void joinAuction(String ClientOperatorCode) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(ClientOperatorCode);
        Client client=clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        Double count= 1000.0;
        //todo 1.判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getCapital() >= count;
        //todo 2.提交申请
        if (isEnoughCapital) {
            //todo 2.1.冻结部分资金
            capitalAccount.setAvailableCapital(capitalAccount.getCapital() - count);
            capitalAccount.setUnavailableCapital(capitalAccount.getCapital() + count);
            //todo 2.2.更新资金账户
            capitalAccountMapper.updateById(capitalAccount);
        }
    }

    @Override
    public void submitOffer(AuctionPost auctionPost) {
        //todo 1.提交洽谈出价
        auctionPostMapper.insert(auctionPost);
        //todo 2.更新洽谈出价记录
    }

    @Override
    public void finishPay(AuctionQuota auctionQuota,AuctionPost auctionPost) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(auctionPost.getOperatorCode());
        Client client=clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        //todo 1.判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getAvailableCapital() >= auctionPost.getPrice();
        //todo 2.达成交易
        if (isEnoughCapital) {
            //todo 2.1 更新资金账户
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - auctionPost.getPrice());
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + auctionPost.getPrice());
            capitalAccountMapper.updateById(capitalAccount);
            //todo 2.2 更新单向竞价商品状态
            auctionQuota.setStatus("已成交");
            auctionQuotaMapper.updateById(auctionQuota);
            //todo 2.3 插入单向竞价成交记录
            //todo 2.3.1新建一条成交记录并获取信息
            AuctionDoneRecord auctionDoneRecord=new AuctionDoneRecord();
            Timestamp originalTime = auctionQuota.getTime();
            long originalTimeInMillis = originalTime.getTime();
            long oneHourLaterInMillis = originalTimeInMillis + 3600000; // 加上一个小时的毫秒数
            Timestamp oneHourLater = new Timestamp(oneHourLaterInMillis);
            auctionDoneRecord.setTime(oneHourLater);
            auctionDoneRecord.setSubjectMatterCode(auctionQuota.getSubjectMatterCode());
            auctionDoneRecord.setSubjectMatterName(auctionQuota.getSubjectMatterName());
            auctionDoneRecord.setFinallyBalance(auctionPost.getPrice());
            auctionDoneRecord.setRequestClient(auctionQuota.getClientId());
            auctionDoneRecord.setPurchaserClient(clientOperator.getClientId());
            //todo 2.3.2插入记录
            auctionDoneRecordMapper.insert(auctionDoneRecord);
        }
    }

}
