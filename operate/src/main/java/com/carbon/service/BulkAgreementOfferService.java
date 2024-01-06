package com.carbon.service;

import com.carbon.input.DirectionPost;
import com.carbon.input.GroupPost;
import com.carbon.po.DirectionDoneRecord;
import com.carbon.po.GroupDoneRecord;

import java.util.List;

//大宗协议报价控制类
public interface BulkAgreementOfferService {
    //定向报价
    boolean directionOffer(DirectionPost directionPost);
    //群组报价
    boolean groupOffer(GroupPost groupPost );
    //修改委托
    DirectionPost modifyDirectionOffer(String id);
    GroupPost modifyGroupOffer(String id);
    //撤销委托
    boolean cancelDirectionOffer(String directionPostId);
    boolean cancelGroupOffer(String groupPostId);
    //定向报价查询
    List<DirectionPost> selectDirectionOfferInfo(String operatorCode);
    //群组报价查询
    List<GroupPost> selectGroupOfferInfo(String operatorCode);
    //定向成交查询
    List<DirectionDoneRecord> selectDirectionBargainInfo(String operatorCode);
    //群组成交查询
    List<GroupDoneRecord> selectGroupBargainInfo(String operatorCode);
}