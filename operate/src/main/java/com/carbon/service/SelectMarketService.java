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
    List<AuctionDoneRecord> selectAuctionDoneRecord(String subjectMatterCode, Date beginTime, Date endTime);

    //查询当日定向报价成交记录
    List<DirectionDoneRecord> selectDirectionDoneRecord(String subjectMatterCode, Date beginTime, Date endTime);

    //查询当日群组报价成交记录
    List<GroupDoneRecord> selectGroupDoneRecord(String subjectMatterCode, Date beginTime, Date endTime);

    //查询当日挂牌交易成交记录
    List<ListingDoneRecord> selectListingDoneRecord(String subjectMatterCode, Date beginTime, Date endTime);
}
