package com.carbon.service.Impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.utils.toolUtils.StringUtils;
import com.carbon.input.Auction.AuctionRequest;
import com.carbon.input.BulkAgreement.DirectionEnquiryPost;
import com.carbon.input.BulkAgreement.DirectionPost;
import com.carbon.input.BulkAgreement.GroupEnquiryPost;
import com.carbon.input.BulkAgreement.GroupPost;
import com.carbon.input.Listing.ListingPost;
import com.carbon.mapper.*;
import com.carbon.output.BulkAgreement.DirectionAndGroupDoneRecordResult;
import com.carbon.output.BulkAgreement.DirectionAndGroupEnquiryPostResult;
import com.carbon.output.BulkAgreement.DirectionAndGroupPostResult;
import com.carbon.po.Auction.AuctionDoneRecord;
import com.carbon.po.BulkAgreement.DirectionDoneRecord;
import com.carbon.po.BulkAgreement.GroupDoneRecord;
import com.carbon.po.Listing.ListingDoneRecord;
import com.carbon.service.TradeSelectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeSelectServiceImpl implements TradeSelectService {
    @Autowired
    ListingPostMapper listingPostMapper;
    @Autowired
    ListingDoneRecordMapper listingDoneRecordMapper;
    @Autowired
    DirectionPostMapper directionPostMapper;
    @Autowired
    GroupPostMapper groupPostMapper;
    @Autowired
    DirectionEnquiryPostMapper directionEnquiryPostMapper;
    @Autowired
    GroupEnquiryPostMapper groupEnquiryPostMapper;
    @Autowired
    DirectionDoneRecordMapper directionDoneRecordMapper;
    @Autowired
    GroupDoneRecordMapper groupDoneRecordMapper;
    @Autowired
    AuctionRequestMapper auctionRequestMapper;
    @Autowired
    AuctionDoneRecordMapper auctionDoneRecordMapper;

    @Override
    public List<ListingPost> selectListingPost(String operatorCode, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType) {
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("operator_code",operatorCode);

        if(StringUtils.isNotEmpty(subjectMatterCode))
            query.eq("subject_matter_code",subjectMatterCode);

        if(beginTime!=null)
            query.ge("begin_time",beginTime);

        if(endTime!=null)
            query.le("end_time",endTime);

        if(StringUtils.isNotEmpty(flowType))
            query.eq("flow_type",flowType);

        return listingPostMapper.selectList(query);
    }

    @Override
    public List<ListingDoneRecord> selectListingDoneRecord(String clientId, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType) {
        QueryWrapper query = new QueryWrapper<ListingDoneRecord>();
        query.eq("client_id",clientId);

        if(StringUtils.isNotEmpty(subjectMatterCode))
            query.eq("subject_matter_code",subjectMatterCode);

        if(beginTime!=null)
            query.ge("begin_time",beginTime);

        if(endTime!=null)
            query.le("end_time",endTime);

        List<ListingDoneRecord> list = listingDoneRecordMapper.selectList(query);

        if(StringUtils.isNotEmpty(flowType))
            list=list.stream().filter((p) -> {
                return (p.getListingClient().equals(clientId) && p.getFlowType().equals(flowType))
                    || (p.getDelistingClient().equals(clientId) && !p.getFlowType().equals(flowType));
            }).collect(Collectors.toList());
        // 挂牌买入y 挂牌卖出n 摘牌买入n 摘牌卖出y 买入
        return list;
    }

    @Override
    public List<DirectionAndGroupPostResult> selectDirectionAndGroupPost(String operatorCode, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType) {
        List<DirectionPost> directionList = directionPostMapper.selectList(null);
        List<GroupPost> groupList = groupPostMapper.selectList(null);
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
    public List<DirectionAndGroupEnquiryPostResult> selectDirectionAndGroupEnquiryPost(String operatorCode, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType) {
        List<DirectionEnquiryPost> directionEnquiryPostList = directionEnquiryPostMapper.selectList(null);
        List<GroupEnquiryPost> groupEnquiryList = groupEnquiryPostMapper.selectList(null);
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
    public List<DirectionAndGroupDoneRecordResult> selectDirectionAndGroupDoneRecord(String clientId, String subjectMatterCode, Timestamp beginTime, Timestamp endTime, String flowType) {
        List<DirectionDoneRecord> directionDoneRecordList = directionDoneRecordMapper.selectList(null);
        List<GroupDoneRecord> groupDoneRecordList = groupDoneRecordMapper.selectList(null);
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
    public List<AuctionRequest> selectAuctionRequest(String operatorCode, String subjectMatterCode, Timestamp beginTime, Timestamp endTime) {
        QueryWrapper query = new QueryWrapper<AuctionRequest>();

        query.eq("operator_code",operatorCode);

        if(StringUtils.isNotEmpty(subjectMatterCode))
            query.eq("subject_matter_code", subjectMatterCode);

        if(beginTime!=null)
            query.ge("begin_time",beginTime);

        if(endTime!=null)
            query.le("end_time",endTime);

        return listingPostMapper.selectList(query);
    }

    @Override
    public List<AuctionDoneRecord> selectAuctionDoneRecord(String clientId, String subjectMatterCode, Timestamp beginTime, Timestamp endTime) {
        QueryWrapper query = new QueryWrapper<AuctionDoneRecord>()
                .eq("request_client",clientId).or().eq("purchaser_client",clientId);

        if(StringUtils.isNotEmpty(subjectMatterCode))
            query.eq("subject_matter_code",subjectMatterCode);

        if(beginTime!=null)
            query.ge("begin_time",beginTime);

        if(endTime!=null)
            query.le("end_time",endTime);

        return listingPostMapper.selectList(query);
    }
}
