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
    //查询当日单向竞价成交记录
    public List<AuctionDoneRecord> selectAuctionDoneRecordCurrent(){
        return selectMarketDao.selectAuctionDoneRecordByDate(LocalDate.now());
    }
    @Override
    //查询当日定向报价成交记录
    public List<DirectionDoneRecord> selectDirectionDoneRecordCurrent(){
        return selectMarketDao.selectDirectionDoneRecordByDate(LocalDate.now());
    }
    @Override
    //查询当日群组报价成交记录
    public List<GroupDoneRecord> selectGroupDoneRecordCurrent(){
        return selectMarketDao.selectGroupDoneRecordByDate(LocalDate.now());
    }
    @Override
    //查询当日挂牌交易成交记录
    public List<ListingDoneRecord> selectListingDoneRecordCurrent(){
        return selectMarketDao.selectListingDoneRecordByDate(LocalDate.now());
    }
    @Override
    //查询历史单向竞价成交记录
    public List<AuctionDoneRecord> selectAuctionDoneRecordHistory(LocalDate date){
        return selectMarketDao.selectAuctionDoneRecordByDate(date);
    }
    @Override
    //查询历史定向报价成交记录
    public List<DirectionDoneRecord> selectDirectionDoneRecordHistory(LocalDate date){
        return selectMarketDao.selectDirectionDoneRecordByDate(date);
    }
    @Override
    //查询历史群组报价成交记录
    public List<GroupDoneRecord> selectGroupDoneRecordHistory(LocalDate date){
        return selectMarketDao.selectGroupDoneRecordByDate(date);
    }
    @Override
    //查询历史挂牌交易成交记录
    public List<ListingDoneRecord> selectListingDoneRecordHistory(LocalDate date){
        return selectMarketDao.selectListingDoneRecordByDate(date);
    }
}
