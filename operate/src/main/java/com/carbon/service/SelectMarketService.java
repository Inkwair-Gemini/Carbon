package com.carbon.service;

import com.carbon.po.AuctionDoneRecord;
import com.carbon.po.DirectionDoneRecord;
import com.carbon.po.GroupDoneRecord;
import com.carbon.po.ListingDoneRecord;
import org.springframework.context.annotation.DependsOn;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface SelectMarketService {
    List<AuctionDoneRecord> selectAuctionDoneRecordCurrent();
    List<DirectionDoneRecord> selectDirectionDoneRecordCurrent();
    List<GroupDoneRecord> selectGroupDoneRecordCurrent();
    List<ListingDoneRecord> selectListingDoneRecordCurrent();
    List<AuctionDoneRecord> selectAuctionDoneRecordHistory(LocalDate date);
    List<DirectionDoneRecord> selectDirectionDoneRecordHistory(LocalDate date);
    List<GroupDoneRecord> selectGroupDoneRecordHistory(LocalDate date);
    List<ListingDoneRecord> selectListingDoneRecordHistory(LocalDate date);
}
