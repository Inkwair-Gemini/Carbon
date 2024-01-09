package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.input.Auction.AuctionPost;
import com.carbon.input.Auction.AuctionRequest;
import com.carbon.mapper.*;
import com.carbon.po.Auction.AuctionClient;
import com.carbon.po.Auction.AuctionDoneRecord;
import com.carbon.po.Auction.AuctionQuota;
import com.carbon.po.Capital.CapitalAccount;
import com.carbon.po.Listing.ListingDoneRecord;
import com.carbon.po.Quota.ClientTradeQuota;
import com.carbon.po.Quota.QuotaAccount;
import com.carbon.po.User.Client;
import com.carbon.po.User.ClientOperator;
import com.carbon.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

/**
 * @projectName: Carbon
 * @package: com.carbon.service.Impl
 * @className: AuctionServiceImpl
 * @author: Doctor.H
 * @description: 单向竞价业务实现
 * @date: 2023/12/27 19:44
 */
@Service
public class AuctionServiceImpl implements AuctionService {
    @Autowired
    ClientOperatorMapper clientOperatorMapper;
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    CapitalAccountMapper capitalAccountMapper;
    @Autowired
    QuotaAccountMapper quotaAccountMapper;
    @Autowired
    ClientTradeQuotaMapper clientTradeQuotaMapper;
    @Autowired
    AuctionRequestMapper auctionRequestMapper;
    @Autowired
    AuctionQuotaMapper auctionQuotaMapper;
    @Autowired
    AuctionPostMapper auctionPostMapper;
    @Autowired
    AuctionDoneRecordMapper auctionDoneRecordMapper;
    @Autowired
    AuctionClientMapper auctionClientMapper;

    @Override
    public void requestAuction(AuctionRequest auctionRequest) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(auctionRequest.getOperatorCode());
        Client client=clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        QuotaAccount quotaAccount = quotaAccountMapper.selectById(client.getQuotaAccountId());

        QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("quota_account_id", quotaAccount.getId())
                .eq("subject_matter_code", auctionRequest.getSubjectMatterCode());
        ClientTradeQuota clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
        //判断是否有足够的配额
        boolean isEnoughQuota = clientTradeQuota.getAvailableQuotaAmount() >= auctionRequest.getAmount();
        //判断是否有足够的资金
        //boolean isEnoughCapital = capitalAccount.getCapital() >= auctionRequest.getCapital();
        //提交申请
        if (isEnoughQuota/* && isEnoughCapital*/) {
            //冻结部分配额
            QueryWrapper<ClientTradeQuota> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq("quota_account_id", quotaAccount.getId())
                    .eq("subject_matter_code", auctionRequest.getSubjectMatterCode());
            clientTradeQuota.setAvailableQuotaAmount(clientTradeQuota.getAvailableQuotaAmount() - auctionRequest.getAmount());
            clientTradeQuota.setUnavailableQuotaAmount(clientTradeQuota.getUnavailableQuotaAmount() + auctionRequest.getAmount());
            //冻结部分资金
            //capitalAccount.setCapital(capitalAccount.getCapital() - auctionRequest.getCapital());
            //更新配额账户
            clientTradeQuotaMapper.update(clientTradeQuota,updateWrapper);
            //更新资金账户
            //capitalDao.updateCapitalAccount(capitalAccount);
            //添加请求记录
            auctionRequestMapper.insert(auctionRequest);
            //todo 等待申请时间

            //新建单向竞价商品
            AuctionQuota auctionQuota=new AuctionQuota();
            //添加auctionRequest内信息到auctionQuota
            auctionQuota.setClientId(clientOperator.getClientId());
            auctionQuota.setSubjectMatterCode(auctionRequest.getSubjectMatterCode());
            auctionQuota.setSubjectMatterName(auctionRequest.getSubjectMatterName());
            auctionQuota.setPrice(auctionRequest.getPrice());
            auctionQuota.setAmount(auctionRequest.getAmount());
            auctionQuota.setTotalBalance(auctionRequest.getPrice()*auctionRequest.getAmount());
            //todo 添加未来发布时间、状态
            //获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, 2); // 当前时间加两个小时

            auctionQuota.setTime(new Timestamp(calendar.getTimeInMillis()));//添加未来平台发布时间
            auctionQuota.setStatus("未成交");
            //向auctionQuota插入单向竞价商品
            auctionQuotaMapper.insert(auctionQuota);

            //向AuctionClient中插入对象
            auctionClientMapper.insert(new AuctionClient(auctionQuota.getId(),clientOperator.getClientId()));


        }
    }
    @Override
    public  List<AuctionQuota> selectAuctionQuota(){
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        QueryWrapper<AuctionQuota> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("time", beginTime, endTime);
        //queryWrapper.eq("status","未成交");
        List<AuctionQuota> auctionQuotas=auctionQuotaMapper.selectList(queryWrapper);
        return auctionQuotas;
    }
    @Override
    public void joinAuction(String auctionQuotaId,String clientOperatorCode) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(clientOperatorCode);
        Client client=clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        Double count= 1000.0;
        //判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getCapital() >= count;
        //提交申请
        if (isEnoughCapital) {
            //冻结部分资金
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - count);
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + count);
            //更新资金账户
            capitalAccountMapper.updateById(capitalAccount);
            //向AuctionClient中插入对象
            auctionClientMapper.insert(new AuctionClient(auctionQuotaId,clientOperator.getClientId()));
        }
    }

    @Override
    public void leaveAuction(String auctionQuotaId,String clientOperatorCode){
        ClientOperator clientOperator = clientOperatorMapper.selectById(clientOperatorCode);
        Client client=clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        Double count= 1000.0;
        //解冻部分资金
        capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() + count);
        capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() - count);
        //更新资金账户
        capitalAccountMapper.updateById(capitalAccount);
        //删除AuctionClient中对象
        String clientId=client.getId();
        QueryWrapper<AuctionClient> auctionClientQueryWrapper=new QueryWrapper<>();
        auctionClientQueryWrapper.eq("auction_quota_id",auctionQuotaId).eq("client_id",clientId);
        auctionClientMapper.delete(auctionClientQueryWrapper);

    }

    @Override
    public void submitOffer(AuctionPost auctionPost) {
        //判断资金
        ClientOperator clientOperator = clientOperatorMapper.selectById(auctionPost.getOperatorCode());
        Client client=clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        AuctionQuota auctionQuota=auctionQuotaMapper.selectById(auctionPost.getAuctionQuotaId());
        //判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getCapital() >= auctionPost.getPrice();
        //判断是否出价更高
        boolean isHigh=auctionPost.getPrice() >= auctionQuota.getTotalBalance();
        if(isEnoughCapital && isHigh){
            //提交洽谈出价
            auctionPostMapper.insert(auctionPost);
        }
    }

    @Override
    public List<AuctionPost> selectOffer(String auctionQuotaId){
        QueryWrapper<AuctionPost> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("auction_quota_id",auctionQuotaId);
        return auctionPostMapper.selectList(queryWrapper);
    }

    @Override
    public AuctionPost selectHighestOffer(List<AuctionPost> auctionPosts){
        AuctionPost highestBid = auctionPosts.get(0);
        for (AuctionPost auctionPost : auctionPosts) {
            if (auctionPost.getPrice() > highestBid.getPrice()) {
                highestBid = auctionPost;
            }
        }
        return highestBid;
    }

    @Override
    public boolean finishPay(AuctionQuota auctionQuota) {
        //判断是否有人出价
        QueryWrapper<AuctionPost> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("auction_quota_id",auctionQuota.getId());
        List<AuctionPost> auctionPosts=auctionPostMapper.selectList(queryWrapper);
        if(auctionPosts.size()!=0) {//有人出价
            //寻找出价最高的报价单
            AuctionPost highAuctionPost = selectHighestOffer(auctionPosts);
            ClientOperator clientOperator = clientOperatorMapper.selectById(highAuctionPost.getOperatorCode());
            Client client=clientMapper.selectById(clientOperator.getClientId());
            CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
            //判断是否有足够的资金
            boolean isEnoughCapital = capitalAccount.getAvailableCapital() >= highAuctionPost.getPrice();
            //达成交易
            if (isEnoughCapital) {
                //更新资金账户
                capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - highAuctionPost.getPrice());
                capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + highAuctionPost.getPrice());
                capitalAccountMapper.updateById(capitalAccount);
                //更新单向竞价商品状态
                auctionQuota.setStatus("已成交");
                auctionQuotaMapper.updateById(auctionQuota);
                //插入单向竞价成交记录
                //新建一条成交记录并获取信息
                AuctionDoneRecord auctionDoneRecord=new AuctionDoneRecord();
                Timestamp originalTime = auctionQuota.getTime();
                long originalTimeInMillis = originalTime.getTime();
                long oneHourLaterInMillis = originalTimeInMillis + 3600000; // 加上一个小时的毫秒数
                Timestamp oneHourLater = new Timestamp(oneHourLaterInMillis);//默认拍卖时间为1小时
                auctionDoneRecord.setTime(oneHourLater);
                auctionDoneRecord.setSubjectMatterCode(auctionQuota.getSubjectMatterCode());
                auctionDoneRecord.setSubjectMatterName(auctionQuota.getSubjectMatterName());
                auctionDoneRecord.setAmount(auctionQuota.getAmount());
                auctionDoneRecord.setFinallyBalance(highAuctionPost.getPrice());
                auctionDoneRecord.setRequestClient(auctionQuota.getClientId());
                auctionDoneRecord.setPurchaserClient(clientOperator.getClientId());
                //插入记录
                auctionDoneRecordMapper.insert(auctionDoneRecord);
                return true;
            }else
                return false;
        }else{
            //更新单向竞价商品状态
            auctionQuota.setStatus("已结束");
            auctionQuotaMapper.updateById(auctionQuota);
            return false;
        }
    }

}
