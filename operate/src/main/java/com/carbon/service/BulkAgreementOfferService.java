package com.carbon.service;

import com.carbon.input.BulkAgreement.DirectionPost;
import com.carbon.input.BulkAgreement.GroupPost;
import com.carbon.po.BulkAgreement.DirectionDoneRecord;
import com.carbon.po.BulkAgreement.GroupDoneRecord;

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
    //当日定向报价查询
    List<DirectionPost> selectDayDirectionOfferInfo(String clientId);
    //当日群组报价查询
    List<GroupPost> selectDayGroupOfferInfo(String clientId);
    //当日定向成交查询
    List<DirectionDoneRecord> selectDayDirectionBargainInfo(String clientId);
    //当日群组成交查询
    List<GroupDoneRecord> selectDayGroupBargainInfo(String clientId);

}