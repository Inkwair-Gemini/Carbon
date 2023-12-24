package com.carbon.service;

public interface ListingService{
    // 买房挂牌
    boolean purchaserListing();
    // 卖方挂牌
    boolean sellerListing();
    // 买房摘牌
    boolean purchaserDelisting();
    // 卖方摘牌
    boolean sellerDelisting();
    // 当前持仓查询
    void selectPositionInfo();
    // 当前委托查询
    void selectEntrustInfo();
    // 当前成交查询
    void selectBargainInfo();
    // 挂牌撤销
    boolean cancelListing();
    // 闭式自动撤销委托
    void autoCancel();
}
