package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.input.BulkAgreement.DirectionEnquiryPost;
import com.carbon.input.BulkAgreement.DirectionPost;
import com.carbon.input.BulkAgreement.GroupEnquiryPost;
import com.carbon.input.BulkAgreement.GroupPost;
import com.carbon.mapper.*;
import com.carbon.po.Auction.AuctionDoneRecord;
import com.carbon.po.BulkAgreement.DirectionDoneRecord;
import com.carbon.po.BulkAgreement.Group;
import com.carbon.po.BulkAgreement.GroupClient;
import com.carbon.po.BulkAgreement.GroupDoneRecord;
import com.carbon.po.Capital.CapitalAccount;
import com.carbon.po.Quota.ClientTradeQuota;
import com.carbon.po.User.Client;
import com.carbon.po.User.ClientOperator;
import com.carbon.service.BulkAgreementEnquiryService;
import com.carbon.utils.BulkStockUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BulkAgreementEnquiryServiceImpl implements BulkAgreementEnquiryService {
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private DirectionPostMapper directionPostMapper;
    @Autowired
    private ClientOperatorMapper clientOperatorMapper;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private CapitalAccountMapper capitalAccountMapper;
    @Autowired
    private QuotaAccountMapper quotaAccountMapper;
    @Autowired
    private CapitalTradeRecordMapper capitalTradeRecordMapper;
    @Autowired
    private QuotaTradeRecordMapper quotaTradeRecordMapper;
    @Autowired
    private GroupEnquiryPostMapper groupEnquiryPostMapper;
    @Autowired
    private GroupPostMapper groupPostMapper;
    @Autowired
    private GroupDoneRecordMapper groupDoneRecordMapper;
    @Autowired
    private DirectionEnquiryPostMapper directionEnquiryPostMapper;
    @Autowired
    private ClientTradeQuotaMapper clientTradeQuotaMapper;
    @Autowired
    private DirectionDoneRecordMapper directionDoneRecordMapper;
    @Autowired
    private GroupClientMapper groupClientMapper;

    @Override
    public boolean sendDirectionOfferEnquiry(DirectionEnquiryPost directionEnquiryPost) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(directionEnquiryPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        boolean isEnough = false;
        //1.判断买卖方向
        if (directionEnquiryPost.getFlowType().equals("买入")) {
            // 1.1.判断是否有足够的资金
            double availableCapital = capitalAccount.getAvailableCapital();
            if (availableCapital >= directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice()) {
                isEnough = true;
            }
        } else if (directionEnquiryPost.getFlowType().equals("卖出")) {
            // 1.2.判断是否有足够的可用交易配额
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", client.getQuotaAccountId()).eq("subject_matter_code", directionEnquiryPost.getSubjectMatterCode());
            clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
            if (clientTradeQuota.getAvailableQuotaAmount() >= directionEnquiryPost.getAmount()) {
                isEnough = true;
            }
        }

        //  2.涨跌幅幅度限制，大宗协议30%
        LocalDate localDate = LocalDate.now();
        LocalDate yesterday = localDate.minusDays(1);
        Timestamp beginTime = Timestamp.valueOf(yesterday.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(yesterday.atTime(23, 59, 59));
        QueryWrapper<DirectionDoneRecord> directionDoneRecordQueryWrapper = new QueryWrapper<>();
        //  2.1.查询昨日收盘价
        directionDoneRecordQueryWrapper.eq("subject_matter_code", directionEnquiryPost.getSubjectMatterCode()).between("time", beginTime, endTime);
        List<DirectionDoneRecord> directionDoneRecordList = directionDoneRecordMapper.selectList(directionDoneRecordQueryWrapper);
        Double closingPrice = BulkStockUtils.getDirectionClosingPrice(directionDoneRecordList);
        //  2.2.计算开价范围
        Double[] closingPriceRange = BulkStockUtils.getClosingPriceRange(closingPrice);
        //  2.3.判断是否在开价范围内
        boolean isInPriceRange = directionEnquiryPost.getPrice() >= closingPriceRange[0] && directionEnquiryPost.getPrice() <= closingPriceRange[1];

        //  3.提交定向洽谈报价
        if (isEnough && isInPriceRange) {
            directionEnquiryPostMapper.insert(directionEnquiryPost);
            return true;
        } else return false;
    }

    @Override
    public boolean sendGroupOfferEnquiry(GroupEnquiryPost groupEnquiryPost) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(groupEnquiryPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        boolean isEnough = false;
        //1.判断买卖方向
        if (groupEnquiryPost.getFlowType().equals("买入")) {
            // 1.1.判断是否有足够的资金
            double availableCapital = capitalAccount.getAvailableCapital();
            if (availableCapital >= groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice()) {
                isEnough = true;
            }
        } else if (groupEnquiryPost.getFlowType().equals("卖出")) {
            // 1.2.判断是否有足够的可用交易配额
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", client.getQuotaAccountId()).eq("subject_matter_code", groupEnquiryPost.getSubjectMatterCode());
            clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
            if (clientTradeQuota.getAvailableQuotaAmount() >= groupEnquiryPost.getAmount()) {
                isEnough = true;
            }
        }

        //  2.涨跌幅幅度限制，大宗协议30%
        LocalDate localDate = LocalDate.now();
        LocalDate yesterday = localDate.minusDays(1);
        Timestamp beginTime = Timestamp.valueOf(yesterday.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(yesterday.atTime(23, 59, 59));
        QueryWrapper<GroupDoneRecord> groupDoneRecordQueryWrapper = new QueryWrapper<>();
        //  2.1.查询昨日收盘价
        groupDoneRecordQueryWrapper.eq("subject_matter_code", groupEnquiryPost.getSubjectMatterCode()).between("time", beginTime, endTime);
        List<GroupDoneRecord> groupDoneRecordList = groupDoneRecordMapper.selectList(groupDoneRecordQueryWrapper);
        Double closingPrice = BulkStockUtils.getGroupClosingPrice(groupDoneRecordList);
        //  2.2.计算开价范围

        Double[] closingPriceRange = BulkStockUtils.getClosingPriceRange(closingPrice);
        //  2.3.判断是否在开价范围内
        boolean isInPriceRange = groupEnquiryPost.getPrice() >= closingPriceRange[0] && groupEnquiryPost.getPrice() <= closingPriceRange[1];

        //  3.提交定向洽谈报价
        if (isEnough && isInPriceRange) {
            groupEnquiryPostMapper.insert(groupEnquiryPost);
            return true;
        } else return false;
    }

    @Override
    public List<DirectionEnquiryPost> selectDirectionOfferEnquiry(String clientId) {
        //1.获取当天起始时间戳
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        QueryWrapper<DirectionEnquiryPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("direction_client", clientId).between("time", beginTime, endTime);
        return directionEnquiryPostMapper.selectList(queryWrapper);
    }

    @Override
    public List<GroupEnquiryPost> selectGroupOfferEnquiry(String operatorCode) {
        //1.获取当天起始时间戳
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        QueryWrapper<GroupEnquiryPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operator_code", operatorCode).between("time", beginTime, endTime);
        return groupEnquiryPostMapper.selectList(queryWrapper);
    }

    @Override
    public void makeDirectionBargain(String directionEnquiryPostId) {
        DirectionEnquiryPost directionEnquiryPost=directionEnquiryPostMapper.selectById(directionEnquiryPostId);
        //获取摘牌方和挂牌方客户
        Client listingClient = clientMapper.selectById(directionEnquiryPost.getDirectionClient());
        ClientOperator delistingClientOperator = clientOperatorMapper.selectById(directionEnquiryPost.getOperatorCode());
        Client delistingClient = clientMapper.selectById(delistingClientOperator.getClientId());


        CapitalAccount capitalAccount = new CapitalAccount();
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        DirectionDoneRecord directionDoneRecord = new DirectionDoneRecord();
        boolean isEnough = false;
        boolean isCapitalEnough= false;
        boolean isQuotaEnough = false;
        //1.判断买卖方向
        if (directionEnquiryPost.getFlowType().equals("买入")) {
            // 1.1.判断挂牌方是否有足够的配额
            //获取挂牌方配额账户
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", listingClient.getQuotaAccountId()).eq("subject_matter_code", directionEnquiryPost.getSubjectMatterCode());
            clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
            if (clientTradeQuota.getAvailableQuotaAmount() >= directionEnquiryPost.getAmount()) {
                isQuotaEnough = true;
            }
            //获取摘牌方资金账户
            capitalAccount = capitalAccountMapper.selectById(delistingClient.getCapitalAccountId());
            if (capitalAccount.getAvailableCapital() >= directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice()) {
                isCapitalEnough = true;
            }

            if (isQuotaEnough && isCapitalEnough) {
                isEnough = true;
            }
        } else if (directionEnquiryPost.getFlowType().equals("卖出")) {
            //判断摘牌方是否有足够的配额
            //获取摘牌方配额账户
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", delistingClient.getQuotaAccountId()).eq("subject_matter_code", directionEnquiryPost.getSubjectMatterCode());
            clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
            if (clientTradeQuota.getAvailableQuotaAmount() >= directionEnquiryPost.getAmount()) {
                isQuotaEnough = true;
            }
            //获取挂牌方资金账户
            capitalAccount = capitalAccountMapper.selectById(listingClient.getCapitalAccountId());
            if (capitalAccount.getAvailableCapital() >= directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice()) {
                isCapitalEnough = true;
            }
            if (isQuotaEnough && isCapitalEnough) {
                isEnough = true;
            }
        }

        //  2.涨跌幅幅度限制，大宗协议30%
        LocalDate localDate = LocalDate.now();
        LocalDate yesterday = localDate.minusDays(1);
        Timestamp beginTime = Timestamp.valueOf(yesterday.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(yesterday.atTime(23, 59, 59));
        QueryWrapper<DirectionDoneRecord> directionDoneRecordQueryWrapper = new QueryWrapper<>();
        //  2.1.查询昨日收盘价
        directionDoneRecordQueryWrapper.eq("subject_matter_code", directionEnquiryPost.getSubjectMatterCode()).between("time", beginTime, endTime);
        List<DirectionDoneRecord> directionDoneRecordList = directionDoneRecordMapper.selectList(directionDoneRecordQueryWrapper);
        Double closingPrice = BulkStockUtils.getDirectionClosingPrice(directionDoneRecordList);
        //  2.2.计算开价范围
        Double[] closingPriceRange = BulkStockUtils.getClosingPriceRange(closingPrice);
        //  2.3.判断是否在开价范围内
        boolean isInPriceRange = directionEnquiryPost.getPrice() >= closingPriceRange[0] && directionEnquiryPost.getPrice() <= closingPriceRange[1];

        boolean isDone = false;
        if (directionEnquiryPost.getFlowType().equals("买入") && isEnough && isInPriceRange) {
            // 1.1.1.冻结摘牌方部分资金，冻结挂牌方部分配额
            clientTradeQuota.setAvailableQuotaAmount(clientTradeQuota.getAvailableQuotaAmount() - directionEnquiryPost.getAmount());
            clientTradeQuota.setUnavailableQuotaAmount(clientTradeQuota.getUnavailableQuotaAmount() + directionEnquiryPost.getAmount());
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice());
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice());

            // 1.1.2.更新摘牌方资金账户，更新挂牌方配额账户
            capitalAccountMapper.updateById(capitalAccount);
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", listingClient.getQuotaAccountId()).eq("subject_matter_code", directionEnquiryPost.getSubjectMatterCode());
            clientTradeQuotaMapper.update(clientTradeQuota,queryWrapper);

            isDone = true;
        } else if (directionEnquiryPost.getFlowType().equals("卖出") && isEnough && isInPriceRange) {
            // 1.2.1.冻结摘牌方部分配额，冻结挂牌方部分资金
            clientTradeQuota.setAvailableQuotaAmount(clientTradeQuota.getAvailableQuotaAmount() - directionEnquiryPost.getAmount());
            clientTradeQuota.setUnavailableQuotaAmount(clientTradeQuota.getUnavailableQuotaAmount() + directionEnquiryPost.getAmount());
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice());
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice());

            // 1.2.2.更新摘牌方配额账户，更新挂牌方资金账户
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", delistingClient.getQuotaAccountId()).eq("subject_matter_code", directionEnquiryPost.getSubjectMatterCode());
            clientTradeQuotaMapper.update(clientTradeQuota,queryWrapper);
            capitalAccountMapper.updateById(capitalAccount);
            isDone = true;
        }
        if (isDone) {
            // 生成成交记录
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            directionDoneRecord.setTime(timestamp);
            directionDoneRecord.setSubjectMatterCode(directionEnquiryPost.getSubjectMatterCode());
            directionDoneRecord.setSubjectMatterName(directionEnquiryPost.getSubjectMatterName());
            QueryWrapper<DirectionPost> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", directionEnquiryPost.getDirectionPostId());
            List<DirectionPost> directionPostList = directionPostMapper.selectList(queryWrapper);
            DirectionPost directionPost = directionPostList.get(0);
            directionDoneRecord.setFlowType(directionPost.getFlowType());
            directionDoneRecord.setFirstPrice(directionPost.getPrice());
            directionDoneRecord.setFirstAmount(directionPost.getAmount());
            directionDoneRecord.setFirstBalance(directionPost.getAmount()*directionPost.getPrice());
            directionDoneRecord.setFinallyAmount(directionEnquiryPost.getAmount());
            directionDoneRecord.setFinallyPrice(directionEnquiryPost.getPrice());
            directionDoneRecord.setFinallyBalance(directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice());
            directionDoneRecord.setListingClient(listingClient.getId());
            directionDoneRecord.setDelistingClient(delistingClient.getId());
            directionDoneRecordMapper.insert(directionDoneRecord);
        }
    }

    @Override
    public void makeGroupBargain(String groupEnquiryPostId) {
        GroupEnquiryPost groupEnquiryPost=groupEnquiryPostMapper.selectById(groupEnquiryPostId);
        //获取挂牌方与摘牌方
        //挂牌方
        Group group=groupMapper.selectById(groupEnquiryPost.getGroupId());
        Client listingClient= clientMapper.selectById(group.getGroupMaster());
        //摘牌方
        ClientOperator clientOperator=clientOperatorMapper.selectById(groupEnquiryPost.getOperatorCode());
        Client delistingClient=clientMapper.selectById(clientOperator.getClientId());

        CapitalAccount capitalAccount = new CapitalAccount();
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        GroupDoneRecord groupDoneRecord = new GroupDoneRecord();
        boolean isEnough = false;
        boolean isCapitalEnough= false;
        boolean isQuotaEnough = false;

        //1.判断买卖方向
        if (groupEnquiryPost.getFlowType().equals("买入")) {
            // 1.1.判断挂牌方是否有足够的配额
            //获取挂牌方配额账户
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", listingClient.getQuotaAccountId()).eq("subject_matter_code", groupEnquiryPost.getSubjectMatterCode());
            clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
            if (clientTradeQuota.getAvailableQuotaAmount() >= groupEnquiryPost.getAmount()) {
                isQuotaEnough = true;
            }
            //获取摘牌方资金账户
            capitalAccount = capitalAccountMapper.selectById(delistingClient.getCapitalAccountId());
            if (capitalAccount.getAvailableCapital() >= groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice()) {
                isCapitalEnough = true;
            }

            if (isQuotaEnough && isCapitalEnough) {
                isEnough = true;
            }
        } else if (groupEnquiryPost.getFlowType().equals("卖出")) {
            //判断摘牌方是否有足够的配额
            //获取摘牌方配额账户
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", delistingClient.getQuotaAccountId()).eq("subject_matter_code", groupEnquiryPost.getSubjectMatterCode());
            clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
            if (clientTradeQuota.getAvailableQuotaAmount() >= groupEnquiryPost.getAmount()) {
                isQuotaEnough = true;
            }
            //获取挂牌方资金账户
            capitalAccount = capitalAccountMapper.selectById(listingClient.getCapitalAccountId());
            if (capitalAccount.getAvailableCapital() >= groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice()) {
                isCapitalEnough = true;
            }
            if (isQuotaEnough && isCapitalEnough) {
                isEnough = true;
            }
        }

        //  2.涨跌幅幅度限制，大宗协议30%
        LocalDate localDate = LocalDate.now();
        LocalDate yesterday = localDate.minusDays(1);
        Timestamp beginTime = Timestamp.valueOf(yesterday.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(yesterday.atTime(23, 59, 59));
        QueryWrapper<GroupDoneRecord> groupDoneRecordQueryWrapper = new QueryWrapper<>();
        //  2.1.查询昨日收盘价
        groupDoneRecordQueryWrapper.eq("subject_matter_code", groupEnquiryPost.getSubjectMatterCode()).between("time", beginTime, endTime);
        List<GroupDoneRecord> groupDoneRecordList = groupDoneRecordMapper.selectList(groupDoneRecordQueryWrapper);
        Double closingPrice = BulkStockUtils.getGroupClosingPrice(groupDoneRecordList);
        //  2.2.计算开价范围
        Double[] closingPriceRange = BulkStockUtils.getClosingPriceRange(closingPrice);
        //  2.3.判断是否在开价范围内
        boolean isInPriceRange = groupEnquiryPost.getPrice() >= closingPriceRange[0] && groupEnquiryPost.getPrice() <= closingPriceRange[1];

        boolean isDone = false;
        if (groupEnquiryPost.getFlowType().equals("买入") && isEnough && isInPriceRange) {
            // 1.1.1.冻结摘牌方部分资金，冻结挂牌方部分配额
            clientTradeQuota.setAvailableQuotaAmount(clientTradeQuota.getAvailableQuotaAmount() - groupEnquiryPost.getAmount());
            clientTradeQuota.setUnavailableQuotaAmount(clientTradeQuota.getUnavailableQuotaAmount() + groupEnquiryPost.getAmount());
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice());
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice());

            // 1.1.2.更新摘牌方资金账户，更新挂牌方配额账户
            capitalAccountMapper.updateById(capitalAccount);
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", listingClient.getQuotaAccountId()).eq("subject_matter_code", groupEnquiryPost.getSubjectMatterCode());
            clientTradeQuotaMapper.update(clientTradeQuota,queryWrapper);

            isDone = true;
        } else if (groupEnquiryPost.getFlowType().equals("卖出") && isEnough && isInPriceRange) {
            // 1.2.1.冻结摘牌方部分配额，冻结挂牌方部分资金
            clientTradeQuota.setAvailableQuotaAmount(clientTradeQuota.getAvailableQuotaAmount() - groupEnquiryPost.getAmount());
            clientTradeQuota.setUnavailableQuotaAmount(clientTradeQuota.getUnavailableQuotaAmount() + groupEnquiryPost.getAmount());
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice());
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice());

            // 1.2.2.更新摘牌方配额账户，更新挂牌方资金账户
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", delistingClient.getQuotaAccountId()).eq("subject_matter_code", groupEnquiryPost.getSubjectMatterCode());
            clientTradeQuotaMapper.update(clientTradeQuota,queryWrapper);
            capitalAccountMapper.updateById(capitalAccount);
            isDone = true;
        }

        if (isDone) {
            // 生成成交记录
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            groupDoneRecord.setTime(timestamp);
            groupDoneRecord.setSubjectMatterCode(groupEnquiryPost.getSubjectMatterCode());
            groupDoneRecord.setSubjectMatterName(groupEnquiryPost.getSubjectMatterName());
            QueryWrapper<GroupPost> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("group_id", groupEnquiryPost.getGroupId());
            List<GroupPost> groupPostList = groupPostMapper.selectList(queryWrapper);
            GroupPost groupPost = groupPostList.get(0);
            groupDoneRecord.setFlowType(groupPost.getFlowType());
            groupDoneRecord.setFirstPrice(groupPost.getPrice());
            groupDoneRecord.setFirstAmount(groupPost.getAmount());
            groupDoneRecord.setFirstBalance(groupPost.getAmount()*groupPost.getPrice());
            groupDoneRecord.setFinallyAmount(groupEnquiryPost.getAmount());
            groupDoneRecord.setFinallyPrice(groupEnquiryPost.getPrice());
            groupDoneRecord.setFinallyBalance(groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice());
            groupDoneRecord.setListingClient(listingClient.getId());
            groupDoneRecord.setDelistingClient(delistingClient.getId());
            groupDoneRecordMapper.insert(groupDoneRecord);
        }
    }

    @Override
    public List<DirectionEnquiryPost> selectDayDirectionOfferEnquiry(String clientId) {
        //1.获取所有客户操作员
        Map<String, Object> clientOperatormap = new HashMap<>();
        clientOperatormap.put("client_id", clientId);
        List<ClientOperator> clientOperators = clientOperatorMapper.selectByMap(clientOperatormap);
        List<DirectionEnquiryPost> directionEnquiryPosts = new ArrayList<>();
        //2.获取当天起始时间戳
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        //3.获取所有操作员的记录
        for (int i = 0; i < clientOperators.size(); i++) {
            QueryWrapper<DirectionEnquiryPost> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("operator_code", clientOperators.get(i).getId()).between("time", beginTime, endTime);
            directionEnquiryPosts.addAll(directionEnquiryPostMapper.selectList(queryWrapper));
        }

        return directionEnquiryPosts;

    }

    @Override
    public List<GroupEnquiryPost> selectDayGroupOfferEnquiry(String clientId) {
        //1.获取所有客户操作员
        Map<String, Object> clientOperatormap = new HashMap<>();
        clientOperatormap.put("client_id", clientId);
        List<ClientOperator> clientOperators = clientOperatorMapper.selectByMap(clientOperatormap);
        List<GroupEnquiryPost> groupEnquiryPosts = new ArrayList<>();
        //2.获取当天起始时间戳
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        //3.获取所有操作员的记录
        for (int i = 0; i < clientOperators.size(); i++) {
            QueryWrapper<GroupEnquiryPost> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("operator_code", clientOperators.get(i).getId()).between("time", beginTime, endTime);
            groupEnquiryPosts.addAll(groupEnquiryPostMapper.selectList(queryWrapper));
        }
        return groupEnquiryPosts;
    }
}
