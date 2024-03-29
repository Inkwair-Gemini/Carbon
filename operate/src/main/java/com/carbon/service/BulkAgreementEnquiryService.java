package com.carbon.service;

import com.carbon.input.BulkAgreement.DirectionEnquiryPost;
import com.carbon.input.BulkAgreement.GroupEnquiryPost;

import java.util.List;

//大宗协议询价控制类
public interface BulkAgreementEnquiryService {
    //定向报价洽谈出价
    boolean sendDirectionOfferEnquiry(DirectionEnquiryPost directionEnquiryPost);
    //群组报价洽谈出价
    boolean sendGroupOfferEnquiry(GroupEnquiryPost groupEnquiryPost);
    //查询定向报价洽谈记录
    List<DirectionEnquiryPost> selectDirectionOfferEnquiry(String clientId);
    //查询群组报价洽谈记录
    List<GroupEnquiryPost> selectGroupOfferEnquiry(String operatorCode);
    //定向达成交易
    void makeDirectionBargain(String directionEnquiryPostId);
    //群组达成交易
    void makeGroupBargain(String groupEnquiryPostId);
    //定向报价当日洽谈记录（询价记录）查询
    List<DirectionEnquiryPost> selectDayDirectionOfferEnquiry(String clientId);
    //群组报价当日洽谈记录（询价记录）查询
    List<GroupEnquiryPost> selectDayGroupOfferEnquiry(String clientId);
}
