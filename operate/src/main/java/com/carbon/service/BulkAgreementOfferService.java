package com.carbon.service;

import com.carbon.input.DirectionPost;
import com.carbon.input.GroupPost;

import java.util.List;

//大宗协议报价控制类
public interface BulkAgreementOfferService {
    //定向报价
    void directionOffer(DirectionPost directionPost);
    //群组报价
    void groupOffer(GroupPost groupPost );
    //修改委托
    DirectionPost modifyOffer(String id);
    //撤销委托
    boolean cancelDirectionOffer(String directionPostId);
    boolean cancelGroupOffer(String groupPostId);
    //报价查询
    List selectOfferInfo();
    //成交查询
    List selectBargainInfo();
}