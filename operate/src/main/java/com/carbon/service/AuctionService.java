package com.carbon.service;

import com.carbon.input.AuctionPost;
import com.carbon.input.AuctionRequest;
import com.carbon.po.AuctionQuota;

public interface AuctionService {
    //申请拍卖
    void requestAuction(AuctionRequest auctionRequest);
    //参加拍卖
    void joinAuction(String ClientOperatorCode);
    //提交竞价
    void submitOffer(AuctionPost auctionPost);
    //结算尾款
    void finishPay(AuctionQuota auctionQuota, AuctionPost auctionPost);

}
