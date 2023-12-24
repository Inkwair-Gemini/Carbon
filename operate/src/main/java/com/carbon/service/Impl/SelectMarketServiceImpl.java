package com.carbon.service.Impl;

import com.carbon.dao.SelectMarketDao;
import com.carbon.po.AuctionDoneRecord;
import com.carbon.po.DirectionDoneRecord;
import com.carbon.po.GroupDoneRecord;
import com.carbon.po.ListingDoneRecord;
import com.carbon.service.SelectMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class SelectMarketServiceImpl implements SelectMarketService {
    @Autowired
    SelectMarketDao selectMarketDao;

    @Override
    public List<AuctionDoneRecord> selectAuctionDoneRecordCurrent(){
        return selectMarketDao.selectAuctionDoneRecordByDate(LocalDate.now());
    }
    @Override
    public List<DirectionDoneRecord> selectDirectionDoneRecordCurrent(){
        return selectMarketDao.selectDirectionDoneRecordByDate(LocalDate.now());
    }
    @Override
    public List<GroupDoneRecord> selectGroupDoneRecordCurrent(){
        return selectMarketDao.selectGroupDoneRecordByDate(LocalDate.now());
    }
    @Override
    public List<ListingDoneRecord> selectListingDoneRecordCurrent(){
        return selectMarketDao.selectListingDoneRecordByDate(LocalDate.now());
    }
    @Override
    public List<AuctionDoneRecord> selectAuctionDoneRecordHistory(LocalDate date){
        return selectMarketDao.selectAuctionDoneRecordByDate(date);
    }
    @Override
    public List<DirectionDoneRecord> selectDirectionDoneRecordHistory(LocalDate date){
        return selectMarketDao.selectDirectionDoneRecordByDate(date);
    }
    @Override
    public List<GroupDoneRecord> selectGroupDoneRecordHistory(LocalDate date){
        return selectMarketDao.selectGroupDoneRecordByDate(date);
    }
    @Override
    public List<ListingDoneRecord> selectListingDoneRecordHistory(LocalDate date){
        return selectMarketDao.selectListingDoneRecordByDate(date);
    }
}
