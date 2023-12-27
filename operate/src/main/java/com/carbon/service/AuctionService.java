package com.carbon.service;

//单向竞价控制类
public interface AuctionService {
    //提交报价单
    void postAuctionOffer();
    //申请拍卖
    void requestAuction();
    //参加拍卖
    void joinAuction();
    //提交竞价
    void submitOffer();
    //结算尾款
    void finishPay();
    //结束拍卖
    void finishAuction();
}
