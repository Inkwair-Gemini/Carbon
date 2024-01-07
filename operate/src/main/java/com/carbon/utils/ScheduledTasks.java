package com.carbon.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.carbon.input.DirectionPost;
import com.carbon.input.GroupPost;
import com.carbon.input.ListingPost;
import com.carbon.mapper.*;
import com.carbon.po.*;
import com.carbon.service.BulkAgreementEnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

/**
 * @projectName: Carbon
 * @package: com.carbon.utils
 * @className: ScheduledTasks
 * @author: Doctor.H
 * @description: 定时任务
 * @date: 2024/1/6 19:14
 */
@Component
@Async
public class ScheduledTasks {
    @Autowired
    private BulkAgreementEnquiryService bulkAgreementEnquiryService;
    @Autowired
    private ListingDoneRecordMapper listingDoneRecordMapper;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private CapitalAccountMapper capitalAccountMapper;
    @Autowired
    private ClientTradeQuotaMapper clientTradeQuotaMapper;
    @Autowired
    private DirectionDoneRecordMapper directionDoneRecordMapper;
    @Autowired
    private GroupDoneRecordMapper groupDoneRecordMapper;
    @Autowired
    private ListingPostMapper listingPostMapper;
    @Autowired
    private DirectionPostMapper directionPostMapper;
    @Autowired
    private GroupPostMapper groupPostMapper;

    @Scheduled(cron = "0 0 16 * * ?")
    public void ListingDoneRecordTask() {
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        QueryWrapper<ListingDoneRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("time", beginTime, endTime);
        List<ListingDoneRecord> listingDoneRecords = listingDoneRecordMapper.selectList(queryWrapper);
        Client listingClient = null;
        Client delistingClient = null;
        CapitalAccount listingCapitalAccount = null;
        CapitalAccount delistingCapitalAccount = null;
        ClientTradeQuota listingClientTradeQuota = null;
        ClientTradeQuota delistingClientTradeQuota = null;
        double dealBalance = 0;
        double dealAmount = 0;
        for (ListingDoneRecord listingDoneRecord : listingDoneRecords) {
            dealBalance = listingDoneRecord.getDealBalance();
            dealAmount = listingDoneRecord.getDealAmount();
            listingClient = clientMapper.selectById(listingDoneRecord.getListingClient());
            delistingClient = clientMapper.selectById(listingDoneRecord.getDelistingClient());
            listingCapitalAccount = capitalAccountMapper.selectById(listingClient.getCapitalAccountId());
            delistingCapitalAccount = capitalAccountMapper.selectById(delistingClient.getCapitalAccountId());
            listingClientTradeQuota = clientTradeQuotaMapper.selectById(listingClient.getQuotaAccountId());
            delistingClientTradeQuota = clientTradeQuotaMapper.selectById(delistingClient.getQuotaAccountId());
            if (listingDoneRecord.getFlowType().equals("买入")) {
                listingCapitalAccount.setUnavailableCapital(listingCapitalAccount.getUnavailableCapital() - dealBalance);
                delistingCapitalAccount.setAvailableCapital(delistingCapitalAccount.getAvailableCapital() + dealBalance);
                listingClientTradeQuota.setAvailableQuotaAmount(listingClientTradeQuota.getAvailableQuotaAmount() + dealAmount);
                delistingClientTradeQuota.setUnavailableQuotaAmount(delistingClientTradeQuota.getUnavailableQuotaAmount() - dealAmount);
            } else if (listingDoneRecord.getFlowType().equals("卖出")) {
                listingCapitalAccount.setAvailableCapital(listingCapitalAccount.getAvailableCapital() + dealBalance);
                delistingCapitalAccount.setUnavailableCapital(delistingCapitalAccount.getUnavailableCapital() - dealBalance);
                listingClientTradeQuota.setUnavailableQuotaAmount(listingClientTradeQuota.getUnavailableQuotaAmount() - dealAmount);
                delistingClientTradeQuota.setAvailableQuotaAmount(delistingClientTradeQuota.getAvailableQuotaAmount() + dealAmount);
            }
        }
        assert listingCapitalAccount != null;
        listingCapitalAccount.setCapital(listingCapitalAccount.getAvailableCapital() + listingCapitalAccount.getUnavailableCapital());
        assert listingClientTradeQuota != null;
        listingClientTradeQuota.setAmount(listingClientTradeQuota.getAvailableQuotaAmount() + listingClientTradeQuota.getUnavailableQuotaAmount());
        assert delistingCapitalAccount != null;
        delistingCapitalAccount.setCapital(delistingCapitalAccount.getAvailableCapital() + delistingCapitalAccount.getUnavailableCapital());
        assert delistingClientTradeQuota != null;
        delistingClientTradeQuota.setAmount(delistingClientTradeQuota.getAvailableQuotaAmount() + delistingClientTradeQuota.getUnavailableQuotaAmount());
    }

    @Scheduled(cron = "0 0 16 * * ?")
    public void DirectionDoneRecordTask() {
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        QueryWrapper<DirectionDoneRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("time", beginTime, endTime);
        List<DirectionDoneRecord> directionDoneRecords = directionDoneRecordMapper.selectList(queryWrapper);
        Client listingClient = null;
        Client delistingClient = null;
        CapitalAccount listingCapitalAccount = null;
        CapitalAccount delistingCapitalAccount = null;
        ClientTradeQuota listingClientTradeQuota = null;
        ClientTradeQuota delistingClientTradeQuota = null;
        double dealBalance = 0;
        double dealAmount = 0;
        for (DirectionDoneRecord directionDoneRecord : directionDoneRecords) {
            dealBalance = directionDoneRecord.getFinallyBalance();
            dealAmount = directionDoneRecord.getFinallyAmount();
            listingClient = clientMapper.selectById(directionDoneRecord.getListingClient());
            delistingClient = clientMapper.selectById(directionDoneRecord.getDelistingClient());
            listingCapitalAccount = capitalAccountMapper.selectById(listingClient.getCapitalAccountId());
            delistingCapitalAccount = capitalAccountMapper.selectById(delistingClient.getCapitalAccountId());
            listingClientTradeQuota = clientTradeQuotaMapper.selectById(listingClient.getQuotaAccountId());
            delistingClientTradeQuota = clientTradeQuotaMapper.selectById(delistingClient.getQuotaAccountId());
            if (directionDoneRecord.getFlowType().equals("买入")) {
                listingCapitalAccount.setUnavailableCapital(listingCapitalAccount.getUnavailableCapital() - dealBalance);
                delistingCapitalAccount.setAvailableCapital(delistingCapitalAccount.getAvailableCapital() + dealBalance);
                listingClientTradeQuota.setAvailableQuotaAmount(listingClientTradeQuota.getAvailableQuotaAmount() + dealAmount);
                delistingClientTradeQuota.setUnavailableQuotaAmount(delistingClientTradeQuota.getUnavailableQuotaAmount() - dealAmount);
            } else if (directionDoneRecord.getFlowType().equals("卖出")) {
                listingCapitalAccount.setAvailableCapital(listingCapitalAccount.getAvailableCapital() + dealBalance);
                delistingCapitalAccount.setUnavailableCapital(delistingCapitalAccount.getUnavailableCapital() - dealBalance);
                listingClientTradeQuota.setUnavailableQuotaAmount(listingClientTradeQuota.getUnavailableQuotaAmount() - dealAmount);
                delistingClientTradeQuota.setAvailableQuotaAmount(delistingClientTradeQuota.getAvailableQuotaAmount() + dealAmount);
            }
        }
        assert listingCapitalAccount != null;
        listingCapitalAccount.setCapital(listingCapitalAccount.getAvailableCapital() + listingCapitalAccount.getUnavailableCapital());
        assert listingClientTradeQuota != null;
        listingClientTradeQuota.setAmount(listingClientTradeQuota.getAvailableQuotaAmount() + listingClientTradeQuota.getUnavailableQuotaAmount());
        assert delistingCapitalAccount != null;
        delistingCapitalAccount.setCapital(delistingCapitalAccount.getAvailableCapital() + delistingCapitalAccount.getUnavailableCapital());
        assert delistingClientTradeQuota != null;
        delistingClientTradeQuota.setAmount(delistingClientTradeQuota.getAvailableQuotaAmount() + delistingClientTradeQuota.getUnavailableQuotaAmount());
    }

    @Scheduled(cron = "0 0 16 * * ?")
    public void GroupDoneRecordTask() {
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        QueryWrapper<GroupDoneRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("time", beginTime, endTime);
        List<GroupDoneRecord> groupDoneRecords = groupDoneRecordMapper.selectList(queryWrapper);
        Client listingClient = null;
        Client delistingClient = null;
        CapitalAccount listingCapitalAccount = null;
        CapitalAccount delistingCapitalAccount = null;
        ClientTradeQuota listingClientTradeQuota = null;
        ClientTradeQuota delistingClientTradeQuota = null;
        double dealBalance = 0;
        double dealAmount = 0;
        for (GroupDoneRecord groupDoneRecord : groupDoneRecords) {
            dealBalance = groupDoneRecord.getFinallyBalance();
            dealAmount = groupDoneRecord.getFinallyAmount();
            listingClient = clientMapper.selectById(groupDoneRecord.getListingClient());
            delistingClient = clientMapper.selectById(groupDoneRecord.getDelistingClient());
            listingCapitalAccount = capitalAccountMapper.selectById(listingClient.getCapitalAccountId());
            delistingCapitalAccount = capitalAccountMapper.selectById(delistingClient.getCapitalAccountId());
            listingClientTradeQuota = clientTradeQuotaMapper.selectById(listingClient.getQuotaAccountId());
            delistingClientTradeQuota = clientTradeQuotaMapper.selectById(delistingClient.getQuotaAccountId());
            if (groupDoneRecord.getFlowType().equals("买入")) {
                listingCapitalAccount.setUnavailableCapital(listingCapitalAccount.getUnavailableCapital() - dealBalance);
                delistingCapitalAccount.setAvailableCapital(delistingCapitalAccount.getAvailableCapital() + dealBalance);
                listingClientTradeQuota.setAvailableQuotaAmount(listingClientTradeQuota.getAvailableQuotaAmount() + dealAmount);
                delistingClientTradeQuota.setUnavailableQuotaAmount(delistingClientTradeQuota.getUnavailableQuotaAmount() - dealAmount);
            } else if (groupDoneRecord.getFlowType().equals("卖出")) {
                listingCapitalAccount.setAvailableCapital(listingCapitalAccount.getAvailableCapital() + dealBalance);
                delistingCapitalAccount.setUnavailableCapital(delistingCapitalAccount.getUnavailableCapital() - dealBalance);
                listingClientTradeQuota.setUnavailableQuotaAmount(listingClientTradeQuota.getUnavailableQuotaAmount() - dealAmount);
                delistingClientTradeQuota.setAvailableQuotaAmount(delistingClientTradeQuota.getAvailableQuotaAmount() + dealAmount);
            }
        }
        assert listingCapitalAccount != null;
        listingCapitalAccount.setCapital(listingCapitalAccount.getAvailableCapital() + listingCapitalAccount.getUnavailableCapital());
        assert listingClientTradeQuota != null;
        listingClientTradeQuota.setAmount(listingClientTradeQuota.getAvailableQuotaAmount() + listingClientTradeQuota.getUnavailableQuotaAmount());
        assert delistingCapitalAccount != null;
        delistingCapitalAccount.setCapital(delistingCapitalAccount.getAvailableCapital() + delistingCapitalAccount.getUnavailableCapital());
        assert delistingClientTradeQuota != null;
        delistingClientTradeQuota.setAmount(delistingClientTradeQuota.getAvailableQuotaAmount() + delistingClientTradeQuota.getUnavailableQuotaAmount());
    }

    @Scheduled(cron = "0 0 16 * * ?")
    public void SystemCancelTask() {
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        //挂牌交易系统撤单
        UpdateWrapper<ListingPost> listingPostUpdateWrapper = new UpdateWrapper<>();
        listingPostUpdateWrapper.between("time", beginTime, endTime);
        listingPostUpdateWrapper.eq("status", "未成交");
        listingPostUpdateWrapper.set("status", "已撤单");
        listingPostMapper.update(null, listingPostUpdateWrapper);
        //定向交易系统撤单
        UpdateWrapper<DirectionPost> directionPostUpdateWrapper = new UpdateWrapper<>();
        directionPostUpdateWrapper.between("time", beginTime, endTime);
        directionPostUpdateWrapper.eq("status", "未成交");
        directionPostUpdateWrapper.set("status", "已撤单");
        directionPostMapper.update(null, directionPostUpdateWrapper);
        //大宗协议交易系统撤单
        UpdateWrapper<GroupPost> groupPostUpdateWrapper = new UpdateWrapper<>();
        groupPostUpdateWrapper.between("time", beginTime, endTime);
        groupPostUpdateWrapper.eq("status", "未成交");
        groupPostUpdateWrapper.set("status", "已撤单");
        groupPostMapper.update(null, groupPostUpdateWrapper);
    }
}
