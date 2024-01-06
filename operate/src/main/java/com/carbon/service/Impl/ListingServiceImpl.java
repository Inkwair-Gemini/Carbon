package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.carbon.input.DelistingPost;
import com.carbon.input.ListingPost;
import com.carbon.mapper.*;
import com.carbon.output.SelectPositionInfoResult;
import com.carbon.po.CapitalAccount;
import com.carbon.po.ClientTradeQuota;
import com.carbon.po.DirectionDoneRecord;
import com.carbon.po.ListingDoneRecord;
import com.carbon.service.ListingService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.sql.Timestamp;
import java.sql.Wrapper;
import java.time.LocalDate;
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
        //只保留数字
        String listingClient=listingPost.getQuotaAccount().replaceAll("[^0-9]","");
        String delistingClient=delistingPost.getQuotaAccount().replaceAll("[^0-9]","");
        listingDoneRecord.setListingClient(listingClient);
        listingDoneRecord.setDelistingClient(delistingClient);
        return listingDoneRecord;
    }
    @Override
    public boolean purchaserListing(ListingPost listingPost) {
        // 限制价格
        LocalDate localDate = LocalDate.now();
        LocalDate yesterday = localDate.minusDays(1);
        Timestamp beginTime = Timestamp.valueOf(yesterday.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(yesterday.atTime(23, 59, 59));
        QueryWrapper<ListingDoneRecord> query=new QueryWrapper<>();
        //查询昨天的成交记录
        query.eq("subject_matter_code",
                listingPost.getSubjectMatterCode()).between("time",beginTime,endTime);
        List<ListingDoneRecord> listingDoneRecordList=ListingDoneRecordMapper.selectList(query);
        if (!listingDoneRecordList.isEmpty()) {
            // 按时间升序对列表进行排序
            listingDoneRecordList.sort(Comparator.comparing(ListingDoneRecord::getTime));
            // 获取最后一条记录
            ListingDoneRecord latestRecord = listingDoneRecordList.get(listingDoneRecordList.size() - 1);
            if(listingPost.getPrice()<listingPost.getPrice()*0.9||listingPost.getPrice()>listingPost.getPrice()*1.1)
                return false;
        }

        // 挂牌交易
        Double availableCapital=CapitalAccountMapper.selectById(listingPost.getQuotaAccount()).getAvailableCapital();
        Double unavailableCapital=CapitalAccountMapper.selectById(listingPost.getQuotaAccount()).getUnavailableCapital();
        if(availableCapital <listingPost.getPrice()*listingPost.getAmount())
            return false;//可用资金不够
        ListingPostMapper.insert(listingPost);
        //更新资金
        UpdateWrapper update = new UpdateWrapper<CapitalAccount>();
        update.eq("id",listingPost.getQuotaAccount());
        update.set("available_capital",availableCapital-listingPost.getPrice()*listingPost.getAmount());
        update.set("unavailable_capital",unavailableCapital+listingPost.getPrice()*listingPost.getAmount());
        CapitalAccountMapper.update(null,update);
        return true;
    }

    @Override
    public boolean sellerListing(ListingPost listingPost) {
        // 限制价格
        LocalDate localDate = LocalDate.now();
        LocalDate yesterday = localDate.minusDays(1);
        Timestamp beginTime = Timestamp.valueOf(yesterday.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(yesterday.atTime(23, 59, 59));
        QueryWrapper<ListingDoneRecord> query=new QueryWrapper<>();
        //查询昨天的成交记录
        query.eq("subject_matter_code",
                listingPost.getSubjectMatterCode()).between("time",beginTime,endTime);
        List<ListingDoneRecord> listingDoneRecordList=ListingDoneRecordMapper.selectList(query);
        if (!listingDoneRecordList.isEmpty()) {
            // 按时间升序对列表进行排序
            listingDoneRecordList.sort(Comparator.comparing(ListingDoneRecord::getTime));
            // 获取最后一条记录
            ListingDoneRecord latestRecord = listingDoneRecordList.get(listingDoneRecordList.size() - 1);
            if(listingPost.getPrice()<listingPost.getPrice()*0.9||listingPost.getPrice()>listingPost.getPrice()*1.1)
                return false;
        }


        QueryWrapper query2 = new QueryWrapper<ListingPost>();
        query2.eq("account_id",listingPost.getQuotaAccount());
        query2.eq("subject_matter_code",listingPost.getSubjectMatterCode());
        Double amount= ClientTradeQuotaMapper.selectOne(query2).getAvailableQuotaAmount();
        Double unamount=ClientTradeQuotaMapper.selectOne(query2).getUnavailableQuotaAmount();
        if(amount<listingPost.getAmount())
            return false;//可用配额不够
        ListingPostMapper.insert(listingPost);
        //更新配额
        UpdateWrapper update = new UpdateWrapper<ClientTradeQuota>();
        update.eq("account_id",listingPost.getQuotaAccount());
        update.eq("subject_matter_code",listingPost.getSubjectMatterCode());
        update.set("available_quota_amount",amount-listingPost.getAmount());
        update.set("unavailable_quota_amount",unamount+listingPost.getAmount());
        ClientTradeQuotaMapper.update(null,update);
        return true;
    }

    @Override
    public boolean purchaserDelisting(DelistingPost delistingPost) {
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("id",delistingPost.getListingId());
        ListingPost listingPost=ListingPostMapper.selectOne(query);
        Double availableCapital=CapitalAccountMapper.selectById(delistingPost.getQuotaAccount()).getAvailableCapital();
        Double unavailableCapital=CapitalAccountMapper.selectById(delistingPost.getQuotaAccount()).getUnavailableCapital();
        if(availableCapital<listingPost.getPrice()*delistingPost.getAmount())
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
        //更新资金
        UpdateWrapper update = new UpdateWrapper<CapitalAccount>();
        update.eq("id",listingPost.getQuotaAccount());
        update.set("available_capital",availableCapital-listingPost.getPrice()*listingPost.getAmount());
        update.set("unavailable_capital",unavailableCapital+listingPost.getPrice()*listingPost.getAmount());
        CapitalAccountMapper.update(null,update);
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
        Double amount= ClientTradeQuotaMapper.selectOne(query).getAvailableQuotaAmount();
        Double unamount=ClientTradeQuotaMapper.selectOne(query).getUnavailableQuotaAmount();
        
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
        //更新配额
        UpdateWrapper update = new UpdateWrapper<ClientTradeQuota>();
        update.eq("account_id",listingPost.getQuotaAccount());
        update.eq("subject_matter_code",listingPost.getSubjectMatterCode());
        update.set("available_quota_amount",amount-listingPost.getAmount());
        update.set("unavailable_quota_amount",unamount+listingPost.getAmount());
        ClientTradeQuotaMapper.update(null,update);
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
        query.eq("quata_account","z"+clientId);
        query.ge("time",temp);
        List listingPostList=ListingPostMapper.selectList(query);

        QueryWrapper query2 = new QueryWrapper<ListingPost>();
        query2.eq("quata_account","q"+clientId);
        query2.ge("time",temp);
        //合并集合
        listingPostList.addAll(ListingPostMapper.selectList(query));
        return listingPostList;
    }

    @Override
    public List<ListingPost> selectEntrustInfo(String clientId,Timestamp start,Timestamp end) {
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("quata_account","z"+clientId);
        query.ge("time",start);
        query.le("time",end);
        List listingPostList=ListingPostMapper.selectList(query);

        QueryWrapper query2 = new QueryWrapper<ListingPost>();
        query2.eq("quata_account","q"+clientId);
        query.ge("time",start);
        query.le("time",end);
        //合并集合
        listingPostList.addAll(ListingPostMapper.selectList(query));
        return listingPostList;
    }

    @Override
    public List<ListingDoneRecord> selectBargainInfo(String clientId) {
        Timestamp temp = new Timestamp(System.currentTimeMillis());
        //把temp设为当天的0点
        temp.setHours(0);
        temp.setMinutes(0);
        temp.setSeconds(0);
        QueryWrapper<ListingDoneRecord> query = new QueryWrapper<ListingDoneRecord>();
        query.eq("listing_client", clientId).or().eq("delisting_client",clientId);
        query.ge("time",temp);
        List listingDoneList=ListingDoneRecordMapper.selectList(query);
        return listingDoneList;
    }

    @Override
    public List<ListingDoneRecord> selectBargainInfo(String clientId,Timestamp start,Timestamp end) {
        QueryWrapper<ListingDoneRecord> query = new QueryWrapper<ListingDoneRecord>();
        query.eq("listing_client", clientId).or().eq("delisting_client",clientId);
        query.ge("time",start);
        query.le("time",end);
        List listingDoneList=ListingDoneRecordMapper.selectList(query);
        return listingDoneList;
    }

    @Override
    public List<ListingPost> selectPurchaserListing() {
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("flow_type","买入");
        query.eq("status","待交易");
        Timestamp temp = new Timestamp(System.currentTimeMillis());
        //把temp设为当天的0点
        temp.setHours(0);
        temp.setMinutes(0);
        temp.setSeconds(0);
        query.ge("time",temp);
        List listingPostList=ListingPostMapper.selectList(query);
        return listingPostList;
    }

    @Override
    public List<ListingPost> selectSellerListing() {
        QueryWrapper query = new QueryWrapper<ListingPost>();
        query.eq("flow_type","卖出");
        query.eq("status","待交易");
        Timestamp temp = new Timestamp(System.currentTimeMillis());
        //把temp设为当天的0点
        temp.setHours(0);
        temp.setMinutes(0);
        temp.setSeconds(0);
        query.ge("time",temp);
        List listingPostList=ListingPostMapper.selectList(query);
        return listingPostList;
    }

    @Override
    public boolean cancelListing(String listingId) {
        UpdateWrapper update = new UpdateWrapper<ListingPost>();
        update.eq("id",listingId);
        update.set("status","已撤销");
        update.set("delisting_time",new Timestamp(System.currentTimeMillis()));
        ListingPostMapper.update(null,update);
        QueryWrapper query=new QueryWrapper<ListingPost>();
        query.eq("id",listingId);
        ListingPost listingPost=ListingPostMapper.selectOne(query);
        //更新配额和资金
        if(listingPost.getFlowType().equals("买入"))
        {
            Double availableCapital=CapitalAccountMapper.selectById(listingPost.getQuotaAccount()).getAvailableCapital();
            Double unavailableCapital=CapitalAccountMapper.selectById(listingPost.getQuotaAccount()).getUnavailableCapital();
            //更新资金
            UpdateWrapper update2 = new UpdateWrapper<CapitalAccount>();
            update2.eq("id",listingPost.getQuotaAccount());
            update2.set("available_capital",availableCapital+listingPost.getPrice()*listingPost.getAmount());
            update2.set("unavailable_capital",unavailableCapital-listingPost.getPrice()*listingPost.getAmount());
            CapitalAccountMapper.update(null,update);
        }
        else
        {
            QueryWrapper query2 = new QueryWrapper<ListingPost>();
            query2.eq("account_id",listingPost.getQuotaAccount());
            query2.eq("subject_matter_code",listingPost.getSubjectMatterCode());
            Double amount= ClientTradeQuotaMapper.selectOne(query2).getAvailableQuotaAmount();
            Double unamount=ClientTradeQuotaMapper.selectOne(query2).getUnavailableQuotaAmount();
            //更新配额
            UpdateWrapper update3 = new UpdateWrapper<ClientTradeQuota>();
            update3.eq("account_id",listingPost.getQuotaAccount());
            update3.eq("subject_matter_code",listingPost.getSubjectMatterCode());
            update3.set("available_quota_amount",amount-listingPost.getAmount());
            update3.set("unavailable_quota_amount",unamount+listingPost.getAmount());
            ClientTradeQuotaMapper.update(null,update3);
        }
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
