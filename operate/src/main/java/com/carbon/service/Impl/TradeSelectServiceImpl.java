package com.carbon.service.Impl;

import com.carbon.Utils.StringUtils;
import com.carbon.input.*;
import com.carbon.output.DirectionAndGroupDoneRecordResult;
import com.carbon.output.DirectionAndGroupEnquiryPostResult;
import com.carbon.output.DirectionAndGroupPostResult;
import com.carbon.po.AuctionDoneRecord;
import com.carbon.po.DirectionDoneRecord;
import com.carbon.po.GroupDoneRecord;
import com.carbon.po.ListingDoneRecord;
import com.carbon.service.TradeSelectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeSelectServiceImpl implements TradeSelectService {
    @Autowired
    TradeSelectDao tradeSelectDao;
    @Autowired
    DirectionAndGroupDao directionAndGroupDao;

    @Override
    public List<ListingPost> selectListingPost(String operatorCode, String subjectMatterCode, Date beginTime, Date endTime, String flowType) {
        List<ListingPost> list = listingTradeDao.selectAllListingPost();

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
        List<ListingDoneRecord> list = listingTradeDao.selectAllListingDoneRecord();

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
    public List<DirectionAndGroupPostResult> selectDirectionAndGroupPost(String operatorCode, String subjectMatterCode, Date beginTime, Date endTime, String flowType) {
        List<DirectionPost> directionList = directionAndGroupDao.selectAllDirectionPost();
        List<GroupPost> groupList = directionAndGroupDao.selectAllGroupPost();
        List<DirectionAndGroupPostResult> list = new ArrayList<DirectionAndGroupPostResult>();
        //添加定向大宗表单
        for (DirectionPost directionPost : directionList) {
            DirectionAndGroupPostResult directionAndGroupPostResult=new DirectionAndGroupPostResult();
            BeanUtils.copyProperties(directionPost,directionAndGroupPostResult);
            directionAndGroupPostResult.setClientOrGroupId(directionPost.getDirectionClient());
            list.add(directionAndGroupPostResult);
        }
        //添加群组大宗表单
        for (GroupPost groupPost : groupList) {
            DirectionAndGroupPostResult directionAndGroupPostResult=new DirectionAndGroupPostResult();
            BeanUtils.copyProperties(groupPost,directionAndGroupPostResult);
            directionAndGroupPostResult.setClientOrGroupId(groupPost.getGroupId());
            list.add(directionAndGroupPostResult);
        }

        list=list.stream().filter(p->p.getOperatorCode().equals(operatorCode)).collect(Collectors.toList());

        if(StringUtils.isNotEmpty(subjectMatterCode))
            list=list.stream().filter(p->p.getSubjectMatterCode().equals(subjectMatterCode)).collect(Collectors.toList());

        if(beginTime!=null)
            list=list.stream().filter(p->p.getTime().after(beginTime)).collect(Collectors.toList());

        if(endTime!=null)
            list=list.stream().filter(p->p.getTime().before(endTime)).collect(Collectors.toList());

        //流转类型是对于操作员来说的
        if(flowType!=null)
            list=list.stream().filter(p->p.getFlowType().equals(flowType)).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<DirectionAndGroupEnquiryPostResult> selectDirectionAndGroupEnquiryPost(String operatorCode, String subjectMatterCode, Date beginTime, Date endTime, String flowType) {
        List<DirectionEnquiryPost> directionEnquiryPostList = directionAndGroupDao.selectAllDirectionEnquiryPost();
        List<GroupEnquiryPost> groupEnquiryList = directionAndGroupDao.selectAllGroupEnquiry();
        List<DirectionAndGroupEnquiryPostResult> list = new ArrayList<>();
        //添加定向大宗表单
        for (DirectionEnquiryPost directionEnquiryPost : directionEnquiryPostList) {
            DirectionAndGroupEnquiryPostResult directionAndGroupEnquiryPostResult=new DirectionAndGroupEnquiryPostResult();
            BeanUtils.copyProperties(directionEnquiryPost,directionAndGroupEnquiryPostResult);
            directionAndGroupEnquiryPostResult.setDirectionOrGroupId(directionEnquiryPost.getDirectionClient());
            list.add(directionAndGroupEnquiryPostResult);
        }
        //添加群组大宗表单
        for (GroupEnquiryPost groupEnquiryPost : groupEnquiryList) {
            DirectionAndGroupEnquiryPostResult directionAndGroupEnquiryPostResult=new DirectionAndGroupEnquiryPostResult();
            BeanUtils.copyProperties(groupEnquiryPost,directionAndGroupEnquiryPostResult);
            directionAndGroupEnquiryPostResult.setDirectionOrGroupId(groupEnquiryPost.getGroupId());
            list.add(directionAndGroupEnquiryPostResult);
        }

        list=list.stream().filter(p->p.getOperatorCode().equals(operatorCode)).collect(Collectors.toList());

        if(StringUtils.isNotEmpty(subjectMatterCode))
            list=list.stream().filter(p->p.getSubjectMatterCode().equals(subjectMatterCode)).collect(Collectors.toList());

        if(beginTime!=null)
            list=list.stream().filter(p->p.getTime().after(beginTime)).collect(Collectors.toList());

        if(endTime!=null)
            list=list.stream().filter(p->p.getTime().before(endTime)).collect(Collectors.toList());

        //流转类型是对于操作员来说的
        if(flowType!=null)
            list=list.stream().filter(p->p.getFlowType().equals(flowType)).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<DirectionAndGroupDoneRecordResult> selectDirectionAndGroupDoneRecord(String clientId, String subjectMatterCode, Date beginTime, Date endTime, String flowType) {
        List<DirectionDoneRecord> directionDoneRecordList = directionAndGroupDao.selectAllDirectionDoneRecord();
        List<GroupDoneRecord> groupDoneRecordList = directionAndGroupDao.selectAllGroupDoneRecord();
        List<DirectionAndGroupDoneRecordResult> list = new ArrayList<>();
        //添加定向大宗表单
        for (DirectionDoneRecord directionDoneRecord : directionDoneRecordList) {
            DirectionAndGroupDoneRecordResult directionAndGroupDoneRecordResult=new DirectionAndGroupDoneRecordResult();
            BeanUtils.copyProperties(directionDoneRecord,directionAndGroupDoneRecordResult);
            list.add(directionAndGroupDoneRecordResult);
        }
        //添加群组大宗表单
        for (GroupDoneRecord groupDoneRecord : groupDoneRecordList) {
            DirectionAndGroupDoneRecordResult directionAndGroupDoneRecordResult=new DirectionAndGroupDoneRecordResult();
            BeanUtils.copyProperties(groupDoneRecord,directionAndGroupDoneRecordResult);
            list.add(directionAndGroupDoneRecordResult);
        }

        list=list.stream().filter(
                p->p.getDelistingClient().equals(clientId)||p.getListingClient().equals(clientId))
                          .collect(Collectors.toList());

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
    public List<AuctionRequest> selectAuctionRequest(String operatorCode, String subjectMatterCode, Date beginTime, Date endTime) {
        List<AuctionRequest> list = actionTradeDao.selectAllActionRequest();

        list=list.stream().filter(
                p->p.getOperatorCode()==operatorCode).collect(Collectors.toList());

        if(StringUtils.isNotEmpty(subjectMatterCode))
            list=list.stream().filter(p->p.getSubjectMatterCode().equals(subjectMatterCode)).collect(Collectors.toList());

        if(beginTime!=null)
            list=list.stream().filter(p->p.getTime().after(beginTime)).collect(Collectors.toList());

        if(endTime!=null)
            list=list.stream().filter(p->p.getTime().before(endTime)).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<AuctionDoneRecord> selectAuctionDoneRecord(String clientId, String subjectMatterCode, Date beginTime, Date endTime) {
        List<AuctionDoneRecord> list = actionTradeDao.selectAllAuctionDoneRecord();

        list=list.stream().filter(
                p->p.getRequestClient().equals(clientId) || p.getPurchaserClient().equals(clientId))
                          .collect(Collectors.toList());

        if(StringUtils.isNotEmpty(subjectMatterCode))
            list=list.stream().filter(p->p.getSubjectMatterCode().equals(subjectMatterCode)).collect(Collectors.toList());

        if(beginTime!=null)
            list=list.stream().filter(p->p.getTime().after(beginTime)).collect(Collectors.toList());

        if(endTime!=null)
            list=list.stream().filter(p->p.getTime().before(endTime)).collect(Collectors.toList());

        return list;
    }
}
