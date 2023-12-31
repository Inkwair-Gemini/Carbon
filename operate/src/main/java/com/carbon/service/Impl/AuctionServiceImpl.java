package com.carbon.service.Impl;

import com.carbon.input.AuctionPost;
import com.carbon.input.AuctionRequest;
import com.carbon.po.*;
import com.carbon.service.AuctionService;

import java.sql.Timestamp;

/**
 * @projectName: Carbon
 * @package: com.carbon.service.Impl
 * @className: AuctionServiceImpl
 * @author: Doctor.H
 * @description: 单向竞价业务实现
 * @date: 2023/12/27 19:44
 */
public class AuctionServiceImpl implements AuctionService {

    @Override
    public void requestAuction(AuctionRequest auctionRequest) {
        ClientOperator clientOperator = clientOperatorDao.selectClientOperatorById(auctionRequest.getOperatorCode());
        CapitalAccount capitalAccount = capitalDao.selectCapitalAccount(clientOperator.getAccountId());
        QuotaAccount quotaAccount = quotaDao.selectQuotaAccount(clientOperator.getAccountId());
        //todo 1.判断是否有足够的配额
        boolean isEnoughQuota = quotaAccount.getQuota() >= auctionRequest.getQuota();
        //todo 2.判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getCapital() >= auctionRequest.getCapital();
        //todo 3.提交申请
        if (isEnoughQuota && isEnoughCapital) {
            //todo 3.1.冻结部分配额
            quotaAccount.setQuota(quotaAccount.getQuota() - auctionRequest.getQuota());
            //todo 3.2.冻结部分资金
            capitalAccount.setCapital(capitalAccount.getCapital() - auctionRequest.getCapital());
            //todo 3.3.更新配额账户
            quotaDao.updateQuotaAccount(quotaAccount);
            //todo 3.4.更新资金账户
            capitalDao.updateCapitalAccount(capitalAccount);
            //todo 3.6.添加请求记录
            auctionRequestDao.insertAuctionRequest(auctionRequest);
            //todo 3.7.等待申请时间

            //todo 3.8.新建单向竞价商品
            AuctionQuota auctionQuota=new AuctionQuota();
            //todo 3.8.1.添加auctionRequest内信息到auctionQuota

            //todo 3.8.2 添加未来发布时间、状态
            auctionQuota.setTime(new Timestamp());//添加未来平台发布时间
            auctionQuota.setStatus("未成交");
            //todo 3.9 向auctionQuota插入单向竞价商品
            auctionQuotaDao.insertAuctionQuota(auctionQuota);

        }
    }

    @Override
    public void joinAuction(String ClientOperatorCode) {
        ClientOperator clientOperator = clientOperatorDao.selectClientOperatorById(ClientOperatorCode);
        CapitalAccount capitalAccount = capitalDao.selectCapitalAccount(clientOperator.getAccountId());
        QuotaAccount quotaAccount = quotaDao.selectQuotaAccount(clientOperator.getAccountId());
        Double count= 1000.0;
        //todo 1.判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getCapital() >= count;
        //todo 2.提交申请
        if (isEnoughCapital) {
            //todo 2.1.冻结部分资金
            capitalAccount.setCapital(capitalAccount.getCapital() - count);
            //todo 2.2.更新资金账户
            capitalDao.updateCapitalAccount(capitalAccount);
        }
    }

    @Override
    public void submitOffer(AuctionPost auctionPost) {
        //todo 1.提交洽谈出价
        auctionPostDao.insertAuctionPost(auctionPost);
        //todo 2.更新洽谈出价记录
    }

    @Override
    public void finishPay(AuctionQuota auctionQuota,AuctionPost auctionPost) {
        ClientOperator clientOperator = clientOperatorDao.selectClientOperatorById(auctionPost.getOperatorCode());
        CapitalAccount capitalAccount = capitalDao.selectCapitalAccount(clientOperator.getAccountId());
        //todo 1.判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getCapital() >= auctionPost.getPrice();
        //todo 2.达成交易
        if (isEnoughCapital) {
            //todo 2.1 更新资金账户
            capitalAccount.setCapital(capitalAccount.getCapital() - directionPost.getCapital());
            capitalDao.updateCapitalAccount(capitalAccount);
            //todo 2.2 更新单向竞价商品状态
            auctionQuota.setStatus("已成交");
            auctionQuotaDao.updateDirectionPost(auctionQuota);
            //todo 2.3 插入单向竞价成交记录
            //todo 2.3.1新建一条成交记录并获取信息
            AuctionDoneRecord auctionDoneRecord=new AuctionDoneRecord();

            //todo 2.3.2插入记录
            auctionDoneRecordDao.insert(auctionDoneRecord);
        }
    }

}
