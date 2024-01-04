package com.carbon.service;

import com.carbon.input.DirectionPost;
import com.carbon.input.GroupEnquiryPost;

//大宗协议询价控制类
public interface BulkAgreementEnquiryService {
    //洽谈出价
    void sendOfferEnquiry(GroupEnquiryPost groupEnquiryPost);
    //达成交易
    void makeBargain(GroupEnquiryPost groupEnquiryPost);
}
