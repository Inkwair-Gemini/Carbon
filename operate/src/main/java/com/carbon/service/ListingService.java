package com.carbon.service;

import com.carbon.input.DelistingPost;
import com.carbon.input.ListingPost;
import com.carbon.output.SelectPositionInfoResult;
import com.carbon.po.ListingDoneRecord;

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
    SelectPositionInfoResult selectPositionInfo(String clientId);
    // 当前委托查询
    List<ListingPost> selectEntrustInfo(String clientId);
    // 当前成交查询
    List<ListingDoneRecord> selectBargainInfo(String clientId);
    // 挂牌撤销
    boolean cancelListing(String listingId);
    // 闭市自动撤销委托
    void autoCancel();
}
