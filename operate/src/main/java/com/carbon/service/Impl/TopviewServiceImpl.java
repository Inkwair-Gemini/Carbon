package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.mapper.ListingDoneRecordMapper;
import com.carbon.output.DealDivision;
import com.carbon.output.TimeDivision;
import com.carbon.po.Listing.ListingDoneRecord;
import com.carbon.service.TopviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

@Service
public class TopviewServiceImpl implements TopviewService {
    @Autowired
    private ListingDoneRecordMapper listingDoneRecordMapper;
    @Override
    public TimeDivision getTimeDivision() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        QueryWrapper<ListingDoneRecord> query = new QueryWrapper<>();
        query.ge("time", now);
        List<ListingDoneRecord> listingDoneRecordList = listingDoneRecordMapper.selectList(query);
        if (listingDoneRecordList.isEmpty()) {
            return null;
        }
        // 按时间升序对列表进行排序
        listingDoneRecordList.sort(Comparator.comparing(ListingDoneRecord::getTime));
        TimeDivision timeDivision = new TimeDivision();
        timeDivision.setPrice(listingDoneRecordList.get(listingDoneRecordList.size()).getDealPrice());
        timeDivision.setTime(new Timestamp(System.currentTimeMillis()));
        double totalAmount = 0;
        double totalPrice = 0;
        for(ListingDoneRecord record : listingDoneRecordList)
        {
            totalAmount += record.getDealAmount();
            totalPrice += record.getDealPrice();
        }
        timeDivision.setAveragePrice(totalPrice / totalAmount);
        return timeDivision;
    }

    @Override
    public List<DealDivision> getDealDivision() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        QueryWrapper<ListingDoneRecord> query = new QueryWrapper<>();
        query.ge("time", now);
        List<ListingDoneRecord> listingDoneRecordList = listingDoneRecordMapper.selectList(query);
        List<ListingDoneRecord> latestRecord;
        List<DealDivision> dealDivisionList = null;
        if (!listingDoneRecordList.isEmpty()) {
            // 按时间升序对列表进行排序
            listingDoneRecordList.sort(Comparator.comparing(ListingDoneRecord::getTime));
            // 获取最后十条记录
            if(listingDoneRecordList.size() <= 10) {
                latestRecord = listingDoneRecordList;
            }
            else {
                latestRecord = listingDoneRecordList.subList
                    (listingDoneRecordList.size() - 10, listingDoneRecordList.size());
            }
            for(ListingDoneRecord record : latestRecord) {
                DealDivision dealDivision = new DealDivision();
                dealDivision.setTime(record.getTime());
                dealDivision.setAmount(record.getDealAmount());
                dealDivision.setPrice(record.getDealPrice());
                dealDivisionList.add(dealDivision);
            }
        }
        return dealDivisionList;
    }
}
