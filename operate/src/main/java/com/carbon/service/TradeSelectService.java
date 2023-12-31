package com.carbon.service;

import com.carbon.input.AuctionRequest;
import com.carbon.input.ListingPost;
import com.carbon.output.DirectionAndGroupDoneRecordResult;
import com.carbon.output.DirectionAndGroupEnquiryPostResult;
import com.carbon.output.DirectionAndGroupPostResult;
import com.carbon.po.AuctionDoneRecord;
import com.carbon.po.ListingDoneRecord;
import java.util.Date;
import java.util.List;

public interface TradeSelectService {
    //挂牌交易历史委托 标的物 日期 方向
    List<ListingPost> selectListingPost(String operatorCode, String subjectMatterCode, Date beginTime, Date endTime, String flowType);
    //挂牌交易历史成交
    List<ListingDoneRecord> selectListingDoneRecord(String clientId, String subjectMatterCode, Date beginTime, Date endTime, String flowType);
    //大宗协议交易报价查询
    List<DirectionAndGroupPostResult> selectDirectionAndGroupPost(String operatorCode, String subjectMatterCode, Date beginTime, Date endTime, String flowType);
    //大宗协议交易询价查询
    List<DirectionAndGroupEnquiryPostResult> selectDirectionAndGroupEnquiryPost(String operatorCode, String subjectMatterCode, Date beginTime, Date endTime, String flowType);

    //大宗协议交易对话流水查询 //todo 准备阉了

    //大宗协议交易询价查询
    List<DirectionAndGroupDoneRecordResult> selectDirectionAndGroupDoneRecord(String operatorCode, String subjectMatterCode, Date beginTime, Date endTime, String flowType);
    //单向竞价交易请求查询
    List<AuctionRequest> selectAuctionRequest(String operatorCode,String subjectMatterCode, Date beginTime, Date endTime);
    //单向竞价交易成交查询
    List<AuctionDoneRecord> selectAuctionDoneRecord(String clientId,String subjectMatterCode, Date beginTime, Date endTime);
}