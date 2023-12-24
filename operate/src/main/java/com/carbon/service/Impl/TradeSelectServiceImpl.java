package com.carbon.service.Impl;

import com.carbon.Utils.StringUtils;
import com.carbon.input.AuctionRequest;
import com.carbon.input.ListingPost;
import com.carbon.output.DirectionAndGroupDoneRecordResult;
import com.carbon.output.DirectionAndGroupEnquiryPostResult;
import com.carbon.output.DirectionAndGroupPostResult;
import com.carbon.po.AuctionDoneRecord;
import com.carbon.po.ListingDoneRecord;
import com.carbon.service.TradeSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeSelectServiceImpl implements TradeSelectService {
    @Autowired
    TradeSelectDao tradeSelectDao;

    @Override
    public List<ListingPost> selectListingPost(String operatorCode, String subjectMatterCode, Date beginTime, Date endTime, String flowType) {
        List<ListingPost> list = tradeSelectDao.selectAllListingPost();

        list=list.stream().filter(p->p.getOperatorCode().equals(operatorCode)).collect(Collectors.toList());

        if(StringUtils.isNotEmpty(subjectMatterCode))
            list=list.stream().filter(p->p.getSubjectMatterCode().equals(subjectMatterCode)).collect(Collectors.toList());

        if(beginTime!=null)
            list=list.stream().filter(p->p.getTime().after(beginTime)).collect(Collectors.toList());

        if(endTime!=null)
            list=list.stream().filter(p->p.getTime().before(endTime)).collect(Collectors.toList());

        if(StringUtils.isNotEmpty(flowType))
            list=list.stream().filter(p-> p.getFlowType().equals(flowType)).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<ListingDoneRecord> selectListingDoneRecord(String clientId, String subjectMatterCode, Date beginTime, Date endTime, String flowType) {
        List<ListingDoneRecord> list = ListingDoneRecord.selectAllListingDoneRecord();

        list=list.stream().filter(
                p->p.getListingClient().equals(clientId) || p.getDelistingClient()
                          .equals(clientId)).collect(Collectors.toList());

        if(StringUtils.isNotEmpty(subjectMatterCode))
            list=list.stream().filter(p->p.getSubjectMatterCode().equals(subjectMatterCode)).collect(Collectors.toList());

        if(beginTime!=null)
            list=list.stream().filter(p->p.getTime().after(beginTime)).collect(Collectors.toList());

        if(endTime!=null)
            list=list.stream().filter(p->p.getTime().before(endTime)).collect(Collectors.toList());

        if(StringUtils.isNotEmpty(flowType))
            list=list.stream().filter((p) -> {
                return (p.getListingClient().equals(clientId) && p.getFlowType().equals(flowType))
                    || (p.getDelistingClient().equals(clientId) && !p.getFlowType().equals(flowType));
            }).collect(Collectors.toList());
        // 挂牌买入y 挂牌卖出n 摘牌买入n 摘牌卖出y 买入
        return list;
    }

    @Override
    public List<DirectionAndGroupPostResult> selectDirectionAndGroupPost(String clientId, String subjectMatterCode, Date beginTime, Date endTime, String flowType) {
        return null;
    }

    @Override
    public List<DirectionAndGroupEnquiryPostResult> selectDirectionAndGroupEnquiryPost(String clientId, String subjectMatterCode, Date beginTime, Date endTime, String flowType) {
        return null;
    }

    @Override
    public List<DirectionAndGroupDoneRecordResult> selectDirectionAndGroupDoneRecord(String clientId, String subjectMatterCode, Date beginTime, Date endTime, String flowType) {
        return null;
    }

    @Override
    public List<AuctionRequest> selectAuctionRequest(String clientId, String subjectMatterCode, Date beginTime, Date endTime) {
        return null;
    }

    @Override
    public List<AuctionDoneRecord> selectAuctionDoneRecord(String clientId, String subjectMatterCode, Date beginTime, Date endTime) {
        return null;
    }
}
