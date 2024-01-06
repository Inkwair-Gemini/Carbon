package com.carbon.service;

import com.carbon.input.DirectionEnquiryPost;
import com.carbon.input.DirectionPost;
import com.carbon.input.GroupEnquiryPost;

import java.util.List;

//大宗协议询价控制类
public interface BulkAgreementEnquiryService {
    //定向报价洽谈出价
    boolean sendDirectionOfferEnquiry(DirectionEnquiryPost directionEnquiryPost);
    //群组报价洽谈出价
    boolean sendGroupOfferEnquiry(GroupEnquiryPost groupEnquiryPost);
    //查询定向报价洽谈记录
    List<DirectionEnquiryPost> selectDirectionOfferEnquiry(String operatorCode);
    //查询群组报价洽谈记录
    List<GroupEnquiryPost> selectGroupOfferEnquiry(String operatorCode);
    //定向达成交易
    void makeDirectionBargain(DirectionEnquiryPost directionEnquiryPost);
    //群组达成交易
    void makeGroupBargain(GroupEnquiryPost groupEnquiryPost);
}
