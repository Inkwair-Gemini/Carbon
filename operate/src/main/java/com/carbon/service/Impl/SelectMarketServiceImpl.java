package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.Utils.StringUtils;
import com.carbon.mapper.AuctionDoneRecordMapper;
import com.carbon.mapper.DirectionDoneRecordMapper;
import com.carbon.mapper.GroupDoneRecordMapper;
import com.carbon.mapper.ListingDoneRecordMapper;
import com.carbon.po.AuctionDoneRecord;
import com.carbon.po.BulkAgreement.DirectionDoneRecord;
import com.carbon.po.GroupDoneRecord;
import com.carbon.po.Listing.ListingDoneRecord;
import com.carbon.service.SelectMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.sql.Timestamp;
@Service
public class SelectMarketServiceImpl implements SelectMarketService {
    @Autowired
    AuctionDoneRecordMapper auctionDoneRecordMapper;
    @Autowired
    DirectionDoneRecordMapper directionDoneRecordMapper;
    @Autowired
    GroupDoneRecordMapper groupDoneRecordMapper;
    @Autowired
    ListingDoneRecordMapper listingDoneRecordMapper;

    @Override
    public List<AuctionDoneRecord> selectAuctionDoneRecord(String subjectMatterCode, Timestamp beginTime, Timestamp endTime) {
        QueryWrapper query = new QueryWrapper<AuctionDoneRecord>();

        if(StringUtils.isNotEmpty(subjectMatterCode))
            query.eq("subject_matter_code",subjectMatterCode);

        if(beginTime!=null)
            query.ge("begin_time",beginTime);

        if(endTime!=null)
            query.le("end_time",endTime);

        return auctionDoneRecordMapper.selectList(query);
    }
    @Override
    public List<DirectionDoneRecord> selectDirectionDoneRecord(String subjectMatterCode, Timestamp beginTime, Timestamp endTime){
        QueryWrapper query = new QueryWrapper<DirectionDoneRecord>();

        if(StringUtils.isNotEmpty(subjectMatterCode))
            query.eq("subject_matter_code",subjectMatterCode);

        if(beginTime!=null)
            query.ge("begin_time",beginTime);

        if(endTime!=null)
            query.le("end_time",endTime);

        return directionDoneRecordMapper.selectList(query);
    }
    @Override
    public List<GroupDoneRecord> selectGroupDoneRecord(String subjectMatterCode, Timestamp beginTime, Timestamp endTime){
        QueryWrapper query = new QueryWrapper<GroupDoneRecord>();

        if(StringUtils.isNotEmpty(subjectMatterCode))
            query.eq("subject_matter_code",subjectMatterCode);

        if(beginTime!=null)
            query.ge("begin_time",beginTime);

        if(endTime!=null)
            query.le("end_time",endTime);

        return groupDoneRecordMapper.selectList(query);
    }
    @Override
    public List<ListingDoneRecord> selectListingDoneRecord(String subjectMatterCode, Timestamp beginTime, Timestamp endTime) {
        QueryWrapper query = new QueryWrapper<ListingDoneRecord>();

        if(StringUtils.isNotEmpty(subjectMatterCode))
            query.eq("subject_matter_code",subjectMatterCode);

        if(beginTime!=null)
            query.ge("begin_time",beginTime);

        if(endTime!=null)
            query.le("end_time",endTime);

        List<ListingDoneRecord> list = listingDoneRecordMapper.selectList(query);
        return list;
    }
}
