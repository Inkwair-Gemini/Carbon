package com.carbon.service;

import com.carbon.po.AuctionDoneRecord;
import com.carbon.po.DirectionDoneRecord;
import com.carbon.po.GroupDoneRecord;
import com.carbon.po.ListingDoneRecord;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface SelectMarketService {

    //查询当日单向竞价成交记录
    List<AuctionDoneRecord> selectAuctionDoneRecordCurrent();

    //查询当日定向报价成交记录
    List<DirectionDoneRecord> selectDirectionDoneRecordCurrent();

    //查询当日群组报价成交记录
    List<GroupDoneRecord> selectGroupDoneRecordCurrent();

    //查询当日挂牌交易成交记录
    List<ListingDoneRecord> selectListingDoneRecordCurrent();

    //查询历史单向竞价成交记录
    List<AuctionDoneRecord> selectAuctionDoneRecordHistory(LocalDate date);

    //查询历史定向报价成交记录
    List<DirectionDoneRecord> selectDirectionDoneRecordHistory(LocalDate date);

    //查询历史群组报价成交记录
    List<GroupDoneRecord> selectGroupDoneRecordHistory(LocalDate date);

    //查询历史挂牌交易成交记录
    List<ListingDoneRecord> selectListingDoneRecordHistory(LocalDate date);
}
