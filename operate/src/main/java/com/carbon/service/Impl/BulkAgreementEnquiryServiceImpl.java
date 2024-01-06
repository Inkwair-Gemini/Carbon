package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.input.DirectionEnquiryPost;
import com.carbon.input.DirectionPost;
import com.carbon.input.GroupEnquiryPost;
import com.carbon.input.GroupPost;
import com.carbon.mapper.*;
import com.carbon.po.*;
import com.carbon.service.BulkAgreementEnquiryService;
import com.carbon.utils.BulkStockUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

/**
 * @projectName: Carbon
 * @package: com.carbon.service.Impl
 * @className: BulkAgreementEnquiryServiceImpl
 * @author: Doctor.H
 * @description: 大宗协议询价业务实现
 * @date: 2023/12/27 21:42
 */
@Service
public class BulkAgreementEnquiryServiceImpl implements BulkAgreementEnquiryService {
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
        QueryWrapper<DirectionDoneRecord> directionDoneRecordQueryWrapper = new QueryWrapper<>();
        //  2.1.查询昨日收盘价
        directionDoneRecordQueryWrapper.eq("subject_matter_code", groupEnquiryPost.getSubjectMatterCode()).between("time", beginTime, endTime);
        List<DirectionDoneRecord> directionDoneRecordList = directionDoneRecordMapper.selectList(directionDoneRecordQueryWrapper);
        Double closingPrice = BulkStockUtils.getDirectionClosingPrice(directionDoneRecordList);
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
    public List<DirectionEnquiryPost> selectDirectionOfferEnquiry(String operatorCode) {
        //1.获取当天起始时间戳
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));
        QueryWrapper<DirectionEnquiryPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operator_code", operatorCode).between("time", beginTime, endTime);
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
    public void makeDirectionBargain(DirectionEnquiryPost directionEnquiryPost) {
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

        boolean isDone = false;
        if (directionEnquiryPost.getFlowType().equals("买入") && isEnough && isInPriceRange) {
            // 1.1.1.冻结部分资金
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice());
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + directionEnquiryPost.getAmount() * directionEnquiryPost.getPrice());
            // 1.1.2.更新资金账户
            capitalAccountMapper.updateById(capitalAccount);
            directionEnquiryPostMapper.insert(directionEnquiryPost);
            isDone = true;
        } else if (directionEnquiryPost.getFlowType().equals("卖出") && isEnough && isInPriceRange) {
            // 1.2.1.冻结部分交易配额
            clientTradeQuota.setAvailableQuotaAmount(clientTradeQuota.getAvailableQuotaAmount() - directionEnquiryPost.getAmount());
            clientTradeQuota.setUnavailableQuotaAmount(clientTradeQuota.getUnavailableQuotaAmount() + directionEnquiryPost.getAmount());
            // 1.2.2.更新交易配额
            clientTradeQuotaMapper.updateById(clientTradeQuota);
            directionEnquiryPostMapper.insert(directionEnquiryPost);
            isDone = true;
        }
    }

    @Override
    public void makeGroupBargain(GroupEnquiryPost groupEnquiryPost) {
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
        QueryWrapper<DirectionDoneRecord> directionDoneRecordQueryWrapper = new QueryWrapper<>();
        //  2.1.查询昨日收盘价
        directionDoneRecordQueryWrapper.eq("subject_matter_code", groupEnquiryPost.getSubjectMatterCode()).between("time", beginTime, endTime);
        List<DirectionDoneRecord> directionDoneRecordList = directionDoneRecordMapper.selectList(directionDoneRecordQueryWrapper);
        Double closingPrice = BulkStockUtils.getDirectionClosingPrice(directionDoneRecordList);
        //  2.2.计算开价范围
        Double[] closingPriceRange = BulkStockUtils.getClosingPriceRange(closingPrice);
        //  2.3.判断是否在开价范围内
        boolean isInPriceRange = groupEnquiryPost.getPrice() >= closingPriceRange[0] && groupEnquiryPost.getPrice() <= closingPriceRange[1];

        boolean isDone = false;
        if (groupEnquiryPost.getFlowType().equals("买入") && isEnough && isInPriceRange) {
            // 1.1.1.冻结部分资金
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice());
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + groupEnquiryPost.getAmount() * groupEnquiryPost.getPrice());
            // 1.1.2.更新资金账户
            capitalAccountMapper.updateById(capitalAccount);
            groupEnquiryPostMapper.insert(groupEnquiryPost);
            isDone = true;
        } else if (groupEnquiryPost.getFlowType().equals("卖出") && isEnough && isInPriceRange) {
            // 1.2.1.冻结部分交易配额
            clientTradeQuota.setAvailableQuotaAmount(clientTradeQuota.getAvailableQuotaAmount() - groupEnquiryPost.getAmount());
            clientTradeQuota.setUnavailableQuotaAmount(clientTradeQuota.getUnavailableQuotaAmount() + groupEnquiryPost.getAmount());
            // 1.2.2.更新交易配额
            clientTradeQuotaMapper.updateById(clientTradeQuota);
            groupEnquiryPostMapper.insert(groupEnquiryPost);
            isDone = true;
        }
    }
}
