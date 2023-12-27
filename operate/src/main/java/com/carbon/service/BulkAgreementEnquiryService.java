package com.carbon.service;

import com.carbon.input.DirectionPost;

//大宗协议询价控制类
public interface BulkAgreementEnquiryService {
    //洽谈出价
    void sendOfferEnquiry(QueryPost queryPost);
    //达成交易
    void makeBargain(DirectionPost directionPost);
}
