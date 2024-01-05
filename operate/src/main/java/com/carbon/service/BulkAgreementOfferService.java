package com.carbon.service;

import com.carbon.input.DirectionPost;
import com.carbon.input.GroupPost;

import java.util.List;

//大宗协议报价控制类
public interface BulkAgreementOfferService {
    //定向报价买入
    void directionBuy(DirectionPost directionPost);
    //定向报价卖出
    void directionSell(DirectionPost directionPost);
    //定向报价
    void directionOffer(DirectionPost directionPost);
    //群组报价
    void groupOffer(GroupPost groupPost );
    //修改委托
    void modifyOffer();
    //撤销委托
    void cancelOffer();
    //报价查询
    List selectOfferInfo();
    //成交查询
    List selectBargainInfo();
}