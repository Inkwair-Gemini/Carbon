package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.po.Auction.AuctionDoneRecord;
import com.carbon.po.BulkAgreement.DirectionDoneRecord;
import com.carbon.po.BulkAgreement.GroupClient;
import com.carbon.po.BulkAgreement.GroupDoneRecord;
import com.carbon.po.Capital.CapitalAccount;
import com.carbon.po.Quota.ClientTradeQuota;
import com.carbon.po.User.Client;
import com.carbon.po.User.ClientOperator;
import com.carbon.utils.BulkStockUtils;
import com.carbon.input.BulkAgreement.DirectionPost;
import com.carbon.input.BulkAgreement.GroupPost;
import com.carbon.mapper.*;
import com.carbon.po.*;
import com.carbon.service.BulkAgreementOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @projectName: Carbon
 * @package: com.carbon.service.Impl
 * @className: BulkAgreementOfferServiceImpl
 * @author: Doctor.H
 * @description: 大宗协议报价业务实现
 * @date: 2023/12/27 19:55
 */
@Service
public class BulkAgreementOfferServiceImpl implements BulkAgreementOfferService {
    @Autowired
    private ClientOperatorMapper clientOperatorMapper;
    @Autowired
    private ClientTradeQuotaMapper clientTradeQuotaMapper;
    @Autowired
    private GroupPostMapper groupPostMapper;
    @Autowired
    private CapitalAccountMapper capitalAccountMapper;
    @Autowired
    private QuotaAccountMapper quotaAccountMapper;
    @Autowired
    private DirectionPostMapper directionPostMapper;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private ClientRegisterQuotaMapper clientRegisterQuotaMapper;
    @Autowired
    private DirectionDoneRecordMapper directionDoneRecordMapper;
    @Autowired
    private GroupDoneRecordMapper groupDoneRecordMapper;
    @Autowired
    private GroupClientMapper groupClientMapper;

    @Override
    public boolean directionOffer(DirectionPost directionPost) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(directionPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        boolean isEnough = false;
        //1.判断买卖方向
        if (directionPost.getFlowType().equals("买入")) {
            // 1.1.判断是否有足够的资金
            double availableCapital = capitalAccount.getCapital();
            if (availableCapital >= directionPost.getAmount() * directionPost.getPrice()) {
                isEnough = true;
            }
        } else if (directionPost.getFlowType().equals("卖出")) {
            // 1.2.判断是否有足够的可用交易配额
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("quota_account_id", client.getQuotaAccountId()).eq("subject_matter_code", directionPost.getSubjectMatterCode());
            clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
            if (clientTradeQuota.getAvailableQuotaAmount() >= directionPost.getAmount()) {
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
        directionDoneRecordQueryWrapper.eq("subject_matter_code", directionPost.getSubjectMatterCode()).between("time", beginTime, endTime);
        List<DirectionDoneRecord> directionDoneRecordList = directionDoneRecordMapper.selectList(directionDoneRecordQueryWrapper);
        Double closingPrice = BulkStockUtils.getDirectionClosingPrice(directionDoneRecordList);
        //  2.2.计算开价范围
        Double[] closingPriceRange = BulkStockUtils.getClosingPriceRange(closingPrice);
        //  2.3.判断是否在开价范围内
        boolean isInPriceRange = directionPost.getPrice() >= closingPriceRange[0] && directionPost.getPrice() <= closingPriceRange[1];

        //  3.判断是否定向报价成功
        if (isEnough && isInPriceRange) {
            //  3.1.定向报价成功
            //  3.1.1.更新资金账户
            directionPost.setTime(new Timestamp(System.currentTimeMillis()));
            directionPostMapper.insert(directionPost);
            return true;
        } else return false;
    }

    @Override
    public boolean groupOffer(GroupPost groupPost) {
        groupPost.setTime(new Timestamp(System.currentTimeMillis()));
        ClientOperator clientOperator = clientOperatorMapper.selectById(groupPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        boolean isEnough = false;
        //1.判断买卖方向
        if (groupPost.getFlowType().equals("买入")) {
            // 1.1.判断是否有足够的资金
            double availableCapital = capitalAccount.getCapital();
            if (availableCapital >= groupPost.getAmount() * groupPost.getPrice()) {
                isEnough = true;
            }
        } else if (groupPost.getFlowType().equals("卖出")) {
            // 1.2.判断是否有足够的登记配额
            QueryWrapper<ClientTradeQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("client_id", client.getId()).eq("subject_matter_code", groupPost.getSubjectMatterCode());
            clientTradeQuota = clientTradeQuotaMapper.selectOne(queryWrapper);
            if (clientTradeQuota.getAmount() >= groupPost.getAmount()) {
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
        groupDoneRecordQueryWrapper.eq("subject_matter_code", groupPost.getSubjectMatterCode()).between("time", beginTime, endTime);
        List<GroupDoneRecord> groupDoneRecordList = groupDoneRecordMapper.selectList(groupDoneRecordQueryWrapper);
        Double closingPrice = BulkStockUtils.getGroupClosingPrice(groupDoneRecordList);
        //  2.2.计算开价范围
        Double[] closingPriceRange = BulkStockUtils.getClosingPriceRange(closingPrice);
        System.out.println(closingPriceRange[0]);
        //  2.3.判断是否在开价范围内
        boolean isInPriceRange = groupPost.getPrice() >= closingPriceRange[0] && groupPost.getPrice() <= closingPriceRange[1];
        System.out.println(isInPriceRange);
        System.out.println(isEnough);
        //  3.判断是否群组报价成功
        if (isEnough && isInPriceRange) {
            //  3.1.群组报价成功
            groupPostMapper.insert(groupPost);
            return true;
        } else return false;
    }

    @Override
    public DirectionPost modifyDirectionOffer(String directionPostId) {
        String operatorCode = directionPostMapper.selectById(directionPostId).getOperatorCode();
        ClientOperator clientOperator = clientOperatorMapper.selectById(operatorCode);
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        //  1.判断报价交易状态
        DirectionPost directionPost = directionPostMapper.selectById(directionPostId);
        if (directionPost.getStatus().equals("已成交") || directionPost.getStatus().equals("已撤销")) {
            //  1.1.报价已成交，无法修改
            return null;
        } else if (directionPost.getStatus().equals("未成交")) {
            //  1.3.报价未成交，可以修改
            //  1.3.1.撤销原报价单，更新原报价单状态
            directionPost.setStatus("已撤销");
            directionPostMapper.updateById(directionPost);
            return directionPost;
        } else return null;
    }

    @Override
    public GroupPost modifyGroupOffer(String groupPostId) {
        String operatorCode = groupPostMapper.selectById(groupPostId).getOperatorCode();
        ClientOperator clientOperator = clientOperatorMapper.selectById(operatorCode);
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        //  1.判断报价交易状态
        GroupPost groupPost = groupPostMapper.selectById(groupPostId);
        if (groupPost.getStatus().equals("已成交") || groupPost.getStatus().equals("已撤销")) {
            //  1.1.报价已成交，无法修改
            return null;
        } else if (groupPost.getStatus().equals("未成交")) {
            //  1.3.报价未成交，可以修改
            //  1.3.1.撤销原报价单，更新报价记录
            groupPost.setStatus("已撤销");
            groupPostMapper.updateById(groupPost);
            return groupPost;
        } else return null;
    }


    @Override
    public boolean cancelDirectionOffer(String directionPostId) {
        DirectionPost directionPost = directionPostMapper.selectById(directionPostId);
        ClientOperator clientOperator = clientOperatorMapper.selectById(directionPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        boolean isCancel = false;
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        //  1.判断报价交易状态
        if (directionPost.getStatus().equals("已成交") || directionPost.getStatus().equals("已撤销")) {
            //  1.1.报价已成交，无法撤销
            return false;
        } else if (directionPost.getStatus().equals("未成交")) {
            //  1.3.报价未成交，可以撤销
            //  1.3.1.撤销报价
            isCancel = true;
            directionPost.setStatus("已撤单");
            directionPostMapper.updateById(directionPost);
        }
        return isCancel;
    }

    @Override
    public boolean cancelGroupOffer(String groupPostId) {
        GroupPost groupPost = groupPostMapper.selectById(groupPostId);
        ClientOperator clientOperator = clientOperatorMapper.selectById(groupPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientTradeQuota clientTradeQuota = new ClientTradeQuota();
        boolean isCancel = false;
        //  1.判断报价交易状态
        if (groupPost.getStatus().equals("已成交") || groupPost.getStatus().equals("已撤销")) {
            //  1.1.报价已成交，无法撤销
            return false;
        } else if (groupPost.getStatus().equals("未成交")) {
            //  1.3.报价未成交，可以撤销
            //  1.3.1.撤销报价
            groupPost.setStatus("已撤单");
            groupPostMapper.updateById(groupPost);
            isCancel = true;
        }
        return isCancel;
    }

    @Override
    public List<DirectionPost> selectDirectionOfferInfo(String operatorCode) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(operatorCode);
        // 1.查询报价记录
        // 操作员发出方的报价记录
        QueryWrapper<DirectionPost> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("operator_code", operatorCode);
        List<DirectionPost> directionPostList1 = directionPostMapper.selectList(queryWrapper1);
        // 作为接收方的报价记录
        QueryWrapper<DirectionPost> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("direction_client", clientOperator.getClientId());
        List<DirectionPost> directionPostList2 = directionPostMapper.selectList(queryWrapper2);
        // 2.返回报价记录
        // 合并记录并删除重复项
        List<DirectionPost> combinedList = Stream.concat(directionPostList1.stream(), directionPostList2.stream())
                .distinct()
                .collect(Collectors.toList());
        return combinedList;
    }

    @Override
    public List<GroupPost> selectGroupOfferInfo(String operatorCode) {
        //获取客户所在的所有群组id
        ClientOperator clientOperator = clientOperatorMapper.selectById(operatorCode);
        QueryWrapper<GroupClient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id", clientOperator.getClientId());
        List<GroupClient> groupClients = groupClientMapper.selectList(queryWrapper);

        //创建一个空的List来存储所有的GroupPost
        List<GroupPost> groupPostList = new ArrayList<>();

        //遍历groupClients，根据每个groupId查询GroupPost
        for (GroupClient groupClient : groupClients) {
            QueryWrapper<GroupPost> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("group_id", groupClient.getGroupId());
            List<GroupPost> posts = groupPostMapper.selectList(queryWrapper1);
            groupPostList.addAll(posts);
        }

        return groupPostList;

    }

    @Override
    public List<DirectionDoneRecord> selectDirectionBargainInfo(String operatorCode) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(operatorCode);
        String clientId = clientOperator.getClientId();
        //  1.查询成交记录
        QueryWrapper<DirectionDoneRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("listing_client", clientId).or().eq("delisting_client", clientId);
        List<DirectionDoneRecord> bargainList = directionDoneRecordMapper.selectList(queryWrapper);
        //  2.返回成交记录
        return bargainList;
    }

    @Override
    public List<GroupDoneRecord> selectGroupBargainInfo(String operatorCode) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(operatorCode);
        String clientId = clientOperator.getClientId();
        //  1.查询成交记录
        QueryWrapper<GroupDoneRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("listing_client", clientId).or().eq("delisting_client", clientId);
        List<GroupDoneRecord> bargainList = groupDoneRecordMapper.selectList(queryWrapper);
        //  2.返回成交记录
        return bargainList;
    }

    //当日定向报价查询
    public List<DirectionPost> selectDayDirectionOfferInfo(String clientId) {
        //1.获取所有客户操作员
        Map<String, Object> clientOperatormap = new HashMap<>();
        clientOperatormap.put("client_id", clientId);
        List<ClientOperator> clientOperators = clientOperatorMapper.selectByMap(clientOperatormap);
        List<DirectionPost> directionPosts = new ArrayList<>();

        //2.获取当日时间戳
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));

        //3.查询所有操作员定向报价记录
        for (int i = 0; i < clientOperators.size(); i++) {
            //操作员发出方的报价记录
            QueryWrapper<DirectionPost> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("operator_code", clientOperators.get(i).getId()).between("time", beginTime, endTime);
            directionPosts.addAll(directionPostMapper.selectList(queryWrapper1));
            //作为接收方的报价记录
            QueryWrapper<DirectionPost> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("direction_client", clientOperators.get(i).getClientId()).between("time", beginTime, endTime);
            directionPosts.addAll(directionPostMapper.selectList(queryWrapper2));
        }

        return directionPosts;

    }

    ;

    //当日群组报价查询
    public List<GroupPost> selectDayGroupOfferInfo(String clientId) {
        //1.获取所有客户操作员
        Map<String, Object> clientOperatormap = new HashMap<>();
        clientOperatormap.put("client_id", clientId);
        List<ClientOperator> clientOperators = clientOperatorMapper.selectByMap(clientOperatormap);
        List<GroupPost> groupPosts = new ArrayList<>();

        //2.获取当日时间戳
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));

        //3.查询所有操作员群组报价记录
        for (int i = 0; i < clientOperators.size(); i++) {
            //获取客户所在的所有群组id
            ClientOperator clientOperator = clientOperatorMapper.selectById(clientOperators.get(i).getId());
            QueryWrapper<GroupClient> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("client_id", clientOperator.getClientId());
            List<GroupClient> groupClients = groupClientMapper.selectList(queryWrapper);

            //遍历groupClients，根据每个groupId查询GroupPost
            for (GroupClient groupClient : groupClients) {
                QueryWrapper<GroupPost> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("group_id", groupClient.getGroupId()).between("time", beginTime, endTime);
                List<GroupPost> posts = groupPostMapper.selectList(queryWrapper1);
                groupPosts.addAll(posts);
            }
        }
        return groupPosts;
    }

    ;

    //当日定向成交查询
    public List<DirectionDoneRecord> selectDayDirectionBargainInfo(String clientId) {
        //1.获取当日时间戳
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));

        //2.查询成交记录
        QueryWrapper<DirectionDoneRecord> listingQueryWrapper = new QueryWrapper<>();
        listingQueryWrapper.eq("listing_client", clientId).between("time", beginTime, endTime);
        List<DirectionDoneRecord> directionDoneRecords = directionDoneRecordMapper.selectList(listingQueryWrapper);

        QueryWrapper<DirectionDoneRecord> delistingQueryWrapper = new QueryWrapper<>();
        delistingQueryWrapper.eq("delisting_client", clientId).between("time", beginTime, endTime);
        directionDoneRecords.addAll(directionDoneRecordMapper.selectList(delistingQueryWrapper));

        return directionDoneRecords;

    }

    ;

    //当日群组成交查询
    public List<GroupDoneRecord> selectDayGroupBargainInfo(String clientId) {
        //1.获取当日时间戳
        LocalDate localDate = LocalDate.now();
        Timestamp beginTime = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(localDate.atTime(23, 59, 59));

        //2.查询成交记录
        QueryWrapper<GroupDoneRecord> listingQueryWrapper = new QueryWrapper<>();
        listingQueryWrapper.eq("listing_client", clientId).between("time", beginTime, endTime);
        List<GroupDoneRecord> groupDoneRecords = groupDoneRecordMapper.selectList(listingQueryWrapper);

        QueryWrapper<GroupDoneRecord> delistingQueryWrapper = new QueryWrapper<>();
        delistingQueryWrapper.eq("delisting_client", clientId).between("time", beginTime, endTime);
        groupDoneRecords.addAll(groupDoneRecordMapper.selectList(delistingQueryWrapper));
        return groupDoneRecords;
    }

    ;

}
