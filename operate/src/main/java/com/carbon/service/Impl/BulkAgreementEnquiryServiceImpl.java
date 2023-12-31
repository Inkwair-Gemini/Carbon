package com.carbon.service.Impl;

import com.carbon.input.DirectionPost;
import com.carbon.input.GroupEnquiryPost;
import com.carbon.po.Group;
import com.carbon.service.BulkAgreementEnquiryService;

/**
 * @projectName: Carbon
 * @package: com.carbon.service.Impl
 * @className: BulkAgreementEnquiryServiceImpl
 * @author: Doctor.H
 * @description: 大宗协议询价业务实现
 * @date: 2023/12/27 21:42
 */
public class BulkAgreementEnquiryServiceImpl implements BulkAgreementEnquiryService {
    @Override
    public void sendOfferEnquiry(GroupEnquiryPost groupEnquiryPost) {
        //todo 1.提交洽谈出价
        GroupEnquiryPostDao.insertGroupEnquiryPost(groupEnquiryPost);
        //todo 2.更新洽谈出价记录
    }

    @Override
    public void makeBargain(DirectionPost directionPost) {
        //todo 1.判断询价交易状态
        //todo 2.判断是否有足够的配额
        boolean isEnoughQuota = quotaAccount.getQuota() >= directionPost.getQuota();
        //todo 3.判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getCapital() >= directionPost.getCapital();
        //todo 4.达成交易
        if (isEnoughQuota && isEnoughCapital) {
            //todo 4.1 更新报价记录的状态
            directionPost.setStatus("已成交");
            directionPostDao.updateDirectionPost(directionPost);
            //todo 4.2 更新配额账户
            quotaAccount.setQuota(quotaAccount.getQuota() - directionPost.getQuota());
            quotaDao.updateQuotaAccount(quotaAccount);
            //todo 4.3 更新资金账户
            capitalAccount.setCapital(capitalAccount.getCapital() - directionPost.getCapital());
            capitalDao.updateCapitalAccount(capitalAccount);
        }
    }
}
