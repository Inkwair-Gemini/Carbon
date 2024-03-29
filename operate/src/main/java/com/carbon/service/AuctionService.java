package com.carbon.service;

import com.carbon.input.Auction.AuctionPost;
import com.carbon.input.Auction.AuctionRequest;
import com.carbon.po.Auction.AuctionDoneRecord;
import com.carbon.po.Auction.AuctionQuota;

import java.sql.Timestamp;
import java.util.List;

public interface AuctionService {
    //申请拍卖
    void requestAuction(AuctionRequest auctionRequest);
    //
    List<AuctionQuota> selectAuctionQuota();
    //参加拍卖
    void joinAuction(String auctionQuotaId,String clientOperatorCode);
    //离开拍卖
    void leaveAuction(String auctionQuotaId,String clientOperatorCode);
    //提交竞价
    void submitOffer(AuctionPost auctionPost);
    //查询竞价记录
    List<AuctionPost> selectOffer(String auctionQuotaId);
    //寻找最高报价单
    AuctionPost selectHighestOffer(List<AuctionPost> auctionPosts);
    //结算尾款
    boolean finishPay(AuctionQuota auctionQuota);
    //查询当日单向竞价委托
    List<AuctionPost> selectDayAuctionPost(String clientId);
    //查询当日单向竞价成交记录
    List<AuctionDoneRecord> selectDayAuctionDoneRecord(String clientId);

}
