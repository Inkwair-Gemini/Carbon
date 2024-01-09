package com.carbon.service;

import com.carbon.input.Auction.AuctionRequest;
import com.carbon.input.Listing.ListingPost;
import com.carbon.output.BulkAgreement.DirectionAndGroupDoneRecordResult;
import com.carbon.output.BulkAgreement.DirectionAndGroupEnquiryPostResult;
import com.carbon.output.BulkAgreement.DirectionAndGroupPostResult;
import com.carbon.po.Auction.AuctionDoneRecord;
import com.carbon.po.Listing.ListingDoneRecord;
import java.util.List;
import java.sql.Timestamp;
public interface TradeSelectService {
    //挂牌交易历史委托 标的物 日期 方向
    List<ListingPost> selectListingPost(String operatorCode, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType);
    //挂牌交易历史成交
    List<ListingDoneRecord> selectListingDoneRecord(String clientId, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType);
    //大宗协议交易报价查询
    List<DirectionAndGroupPostResult> selectDirectionAndGroupPost(String operatorCode, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType);
    //大宗协议交易询价查询
    List<DirectionAndGroupEnquiryPostResult> selectDirectionAndGroupEnquiryPost(String operatorCode, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType);
    //大宗协议交易询价查询
    List<DirectionAndGroupDoneRecordResult> selectDirectionAndGroupDoneRecord(String operatorCode, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType);
    //单向竞价交易请求查询
    List<AuctionRequest> selectAuctionRequest(String operatorCode,String subjectMatterCode, Timestamp beginTime, Timestamp endTime);
    //单向竞价交易成交查询
    List<AuctionDoneRecord> selectAuctionDoneRecord(String clientId,String subjectMatterCode, Timestamp beginTime, Timestamp endTime);
}