package com.carbon.service;

import com.carbon.po.AuctionDoneRecord;
import com.carbon.po.BulkAgreement.DirectionDoneRecord;
import com.carbon.po.GroupDoneRecord;
import com.carbon.po.Listing.ListingDoneRecord;


import java.sql.Timestamp;
import java.util.List;

public interface SelectMarketService {

    //查询当日单向竞价成交记录
    List<AuctionDoneRecord> selectAuctionDoneRecord(String subjectMatterCode, Timestamp beginTime, Timestamp endTime);

    //查询当日定向报价成交记录
    List<DirectionDoneRecord> selectDirectionDoneRecord(String subjectMatterCode, Timestamp beginTime, Timestamp endTime);

    //查询当日群组报价成交记录
    List<GroupDoneRecord> selectGroupDoneRecord(String subjectMatterCode, Timestamp beginTime, Timestamp endTime);

    //查询当日挂牌交易成交记录
    List<ListingDoneRecord> selectListingDoneRecord(String subjectMatterCode, Timestamp beginTime, Timestamp endTime);
}
