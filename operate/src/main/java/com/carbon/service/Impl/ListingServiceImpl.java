package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.carbon.input.DelistingPost;
import com.carbon.input.ListingPost;
import com.carbon.mapper.*;
import com.carbon.output.SelectPositionInfoResult;
import com.carbon.po.ClientTradeQuota;
import com.carbon.po.ListingDoneRecord;
import com.carbon.service.ListingService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.sql.Wrapper;
import java.util.List;

@Service
public class ListingServiceImpl implements ListingService {

    @Autowired
    private ListingPostMapper ListingPostMapper;
    @Autowired
    private ListingDoneRecordMapper ListingDoneRecordMapper;
    @Autowired
    private CapitalAccountMapper CapitalAccountMapper;
    @Autowired
    private QuotaAccountMapper QuotaAccountMapper;
    @Autowired
    private ClientTradeQuotaMapper ClientTradeQuotaMapper;
    @NotNull
    private static ListingDoneRecord setListingDoneRecord(DelistingPost delistingPost, ListingPost listingPost) {
        ListingDoneRecord listingDoneRecord=new ListingDoneRecord();
        listingDoneRecord.setTime(delistingPost.getTime());
        listingDoneRecord.setSubjectMatterCode(listingPost.getSubjectMatterCode());
        listingDoneRecord.setSubjectMatterName(listingPost.getSubjectMatterName());
        listingDoneRecord.setFlowType(delistingPost.getFlowType());
        listingDoneRecord.setDealPrice(listingPost.getPrice());
        listingDoneRecord.setDealAmount(delistingPost.getAmount());
        listingDoneRecord.setDealBalance(listingPost.getPrice()* delistingPost.getAmount());
        listingDoneRecord.setListingClient(listingPost.getOperatorCode());
        listingDoneRecord.setDelistingClient(delistingPost.getOperatorCode());
        return listingDoneRecord;
    }
    @Override
    public boolean purchaserListing(ListingPost listingPost) {
        if(CapitalAccountMapper.selectById(listingPost.getQuotaAccount()).getAvailableCapital()
                <listingPost.getPrice()*listingPost.getAmount())
            return false;//可用资金不够
        ListingPostMapper.insert(listingPost);
        //todo 更新配额和资金
        return true;
    }

    @Override
    public boolean sellerListing(ListingPost listingPost) {
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("account_id",listingPost.getQuotaAccount());
        query.eq("subject_matter_code",listingPost.getSubjectMatterCode());
        Double amount= ClientTradeQuotaMapper.selectOne(query).getAmount();
        if(amount<listingPost.getAmount())
            return false;//可用配额不够
        ListingPostMapper.insert(listingPost);
        //todo 更新配额和资金
        return true;
    }

    @Override
    public boolean purchaserDelisting(DelistingPost delistingPost) {
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("id",delistingPost.getListingId());
        ListingPost listingPost=ListingPostMapper.selectOne(query);
        if(CapitalAccountMapper.selectById(delistingPost.getQuotaAccount()).getAvailableCapital()
                <listingPost.getPrice()*delistingPost.getAmount())
            return false;//可用资金不够
        if(listingPost.getAmount()>delistingPost.getAmount()){
            UpdateWrapper update = new UpdateWrapper<ListingPost>();
            update.eq("id",delistingPost.getListingId());
            update.set("amount",listingPost.getAmount()-delistingPost.getAmount());
            ListingPostMapper.update(null,update);

            ListingDoneRecord listingDoneRecord = setListingDoneRecord(delistingPost, listingPost);
            ListingDoneRecordMapper.insert(listingDoneRecord);
        }
        else{
            UpdateWrapper update = new UpdateWrapper<ListingPost>();
            update.eq("id",delistingPost.getListingId());
            update.set("status","已成交");
            ListingPostMapper.update(null,update);

            ListingDoneRecord listingDoneRecord = setListingDoneRecord(delistingPost, listingPost);
            ListingDoneRecordMapper.insert(listingDoneRecord);
        }
        //todo 更新配额和资金
        return true;
    }


    @Override
    public boolean sellerDelisting(DelistingPost delistingPost) {
        QueryWrapper query2 = new QueryWrapper<ListingPost>();
        query2.eq("id",delistingPost.getListingId());
        ListingPost listingPost=ListingPostMapper.selectOne(query2);
        
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("account_id",delistingPost.getQuotaAccount());
        query.eq("subject_matter_code",listingPost.getSubjectMatterCode());
        Double amount= ClientTradeQuotaMapper.selectOne(query).getAmount();
        
        if(amount<delistingPost.getAmount())
            return false;//可用配额不够
        if(listingPost.getAmount()>delistingPost.getAmount()){
            UpdateWrapper update = new UpdateWrapper<ListingPost>();
            update.eq("id",delistingPost.getListingId());
            update.set("amount",listingPost.getAmount()-delistingPost.getAmount());
            ListingPostMapper.update(null,update);

            ListingDoneRecord listingDoneRecord = setListingDoneRecord(delistingPost, listingPost);
            ListingDoneRecordMapper.insert(listingDoneRecord);
        }
        else{
            UpdateWrapper update = new UpdateWrapper<ListingPost>();
            update.eq("id",delistingPost.getListingId());
            update.set("status","已成交");
            ListingPostMapper.update(null,update);

            ListingDoneRecord listingDoneRecord = setListingDoneRecord(delistingPost, listingPost);
            ListingDoneRecordMapper.insert(listingDoneRecord);
        }
        //todo 更新配额和资金
        return true;
    }

    @Override
    public SelectPositionInfoResult selectPositionInfo(String clientId) {
        SelectPositionInfoResult result = new SelectPositionInfoResult();
        result.setCapitalAccount(CapitalAccountMapper.selectById("z"+clientId));
        result.setQuotaAccount(QuotaAccountMapper.selectById("q"+clientId));
        QueryWrapper query = new QueryWrapper<ClientTradeQuota>();
        query.eq("account_id","q"+clientId);
        List list=ClientTradeQuotaMapper.selectList(query);
        result.setQuotaList(list);
        return result;
    }

    @Override
    public List<ListingPost> selectEntrustInfo(String clientId) {
        Timestamp temp = new Timestamp(System.currentTimeMillis());
        //把temp设为当天的0点
        temp.setHours(0);
        temp.setMinutes(0);
        temp.setSeconds(0);
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("operator_code",clientId);
        query.eq("status","待交易");
        query.ge("time",temp);
        List listingPostList=ListingPostMapper.selectList(query);
        return listingPostList;
    }

    @Override
    public List<ListingDoneRecord> selectBargainInfo(String clientId) {
        Timestamp temp = new Timestamp(System.currentTimeMillis());
        //把temp设为当天的0点
        temp.setHours(0);
        temp.setMinutes(0);
        temp.setSeconds(0);
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("listing_client",clientId);
        query.ge("time",temp);
        List listingDoneList=ListingDoneRecordMapper.selectList(query);
        return listingDoneList;
    }

    @Override
    public boolean cancelListing(String listingId) {
        UpdateWrapper update = new UpdateWrapper<ListingPost>();
        update.eq("id",listingId);
        update.set("status","已撤销");
        update.set("delisting_time",new Timestamp(System.currentTimeMillis()));
        ListingPostMapper.update(null,update);
        return true;
    }

    @Override
    public void autoCancel() {
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("status","待交易");
        List listingPostList=ListingPostMapper.selectList(query);
        for(int i=0;i<listingPostList.size();i++){
            ListingPost listingPost=(ListingPost)listingPostList.get(i);
            UpdateWrapper update = new UpdateWrapper<ListingPost>();
            update.eq("id",listingPost.getId());
            update.set("status","已撤销");
            update.set("delisting_time",new Timestamp(System.currentTimeMillis()));
            ListingPostMapper.update(null,update);
        }
    }
}
