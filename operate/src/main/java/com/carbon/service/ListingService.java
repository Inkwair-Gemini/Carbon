package com.carbon.service;

import com.carbon.input.Listing.DelistingPost;
import com.carbon.input.Listing.ListingPost;
import com.carbon.po.Listing.ListingDoneRecord;

import java.sql.Timestamp;
import java.util.List;

public interface ListingService{
    // 买方挂牌
    boolean purchaserListing(ListingPost listingPost);
    // 卖方挂牌
    boolean sellerListing(ListingPost listingPost);
    // 买方摘牌
    boolean purchaserDelisting(DelistingPost delistingPost);
    // 卖方摘牌
    boolean sellerDelisting(DelistingPost delistingPost);
    // 当前持仓查询
//    SelectPositionInfoResult selectPositionInfo(String clientId);
    // 当前委托查询
    List<ListingPost> selectEntrustInfo(String clientId);
    // 历史委托查询
    List<ListingPost> selectEntrustInfo(String clientId, Timestamp start,Timestamp end);
    // 当前成交查询
    List<ListingDoneRecord> selectBargainInfo(String clientId);
    //历史成交查询
    List<ListingDoneRecord> selectBargainInfo(String clientId,Timestamp start,Timestamp end);
    // 查询买方挂牌
    List<ListingPost> selectPurchaserListing();
    // 查询卖方挂牌
    List<ListingPost> selectSellerListing();
    // 挂牌撤销
    boolean cancelListing(String listingId);
    // 闭市自动撤销委托
    void autoCancel();
}
