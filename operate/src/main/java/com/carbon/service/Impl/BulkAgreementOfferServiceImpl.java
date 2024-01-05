package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.utils.BulkStockUtils;
import com.carbon.input.DirectionPost;
import com.carbon.input.GroupPost;
import com.carbon.mapper.*;
import com.carbon.po.*;
import com.carbon.service.BulkAgreementOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

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
    private GroupClientMapper groupClientMapper;

    @Override
    public void directionOffer(DirectionPost directionPost) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(directionPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientRegisterQuota clientRegisterQuota = new ClientRegisterQuota();
        boolean isEnough = false;
        //1.判断买卖方向
        if (directionPost.getFlowType().equals("买入")) {
            // 1.1.判断是否有足够的资金
            double availableCapital = capitalAccount.getCapital();
            if (availableCapital >= directionPost.getAmount() * directionPost.getPrice()) {
                isEnough = true;
            }
        } else if (directionPost.getFlowType().equals("卖出")) {
            // 1.2.判断是否有足够的登记配额
            QueryWrapper<ClientRegisterQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("client_id", client.getId()).eq("subject_matter_code", directionPost.getSubjectMatterCode());
            clientRegisterQuota = clientRegisterQuotaMapper.selectOne(queryWrapper);
            if (clientRegisterQuota.getAmount() >= directionPost.getAmount()) {
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
        Double closingPrice = BulkStockUtils.getClosingPrice(directionDoneRecordList);
        //  2.2.计算开价范围
        Double[] closingPriceRange = BulkStockUtils.getClosingPriceRange(closingPrice);
        //  2.3.判断是否在开价范围内
        boolean isInPriceRange = directionPost.getPrice() >= closingPriceRange[0] && directionPost.getPrice() <= closingPriceRange[1];

        //  3.提交定向报价
        if (directionPost.getFlowType().equals("买入") && isEnough && isInPriceRange) {
            // 1.1.1.冻结部分资金
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - directionPost.getAmount() * directionPost.getPrice());
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + directionPost.getAmount() * directionPost.getPrice());
            // 1.1.2.更新资金账户
            capitalAccountMapper.updateById(capitalAccount);
        } else if (directionPost.getFlowType().equals("卖出") && isEnough && isInPriceRange) {
            // 1.2.1.冻结部分登记配额
            clientRegisterQuota.setAmount(clientRegisterQuota.getAmount() - directionPost.getAmount());
            // 1.2.2.更新登记配额
            clientRegisterQuotaMapper.updateById(clientRegisterQuota);
        }
        directionPostMapper.insert(directionPost);
    }

    @Override
    public void groupOffer(GroupPost groupPost) {
        ClientOperator clientOperator = clientOperatorMapper.selectById(groupPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientRegisterQuota clientRegisterQuota = new ClientRegisterQuota();
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
            QueryWrapper<ClientRegisterQuota> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("client_id", client.getId()).eq("subject_matter_code", groupPost.getSubjectMatterCode());
            clientRegisterQuota = clientRegisterQuotaMapper.selectOne(queryWrapper);
            if (clientRegisterQuota.getAmount() >= groupPost.getAmount()) {
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
        directionDoneRecordQueryWrapper.eq("subject_matter_code", groupPost.getSubjectMatterCode()).between("time", beginTime, endTime);
        List<DirectionDoneRecord> directionDoneRecordList = directionDoneRecordMapper.selectList(directionDoneRecordQueryWrapper);
        Double closingPrice = BulkStockUtils.getClosingPrice(directionDoneRecordList);
        //  2.2.计算开价范围
        Double[] closingPriceRange = BulkStockUtils.getClosingPriceRange(closingPrice);
        //  2.3.判断是否在开价范围内
        boolean isInPriceRange = groupPost.getPrice() >= closingPriceRange[0] && groupPost.getPrice() <= closingPriceRange[1];

        //  3.提交群组报价
        if (groupPost.getFlowType().equals("买入") && isEnough && isInPriceRange) {
            // 1.1.1.冻结部分资金
            capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() - groupPost.getAmount() * groupPost.getPrice());
            capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() + groupPost.getAmount() * groupPost.getPrice());
            // 1.1.2.更新资金账户
            capitalAccountMapper.updateById(capitalAccount);
        } else if (groupPost.getFlowType().equals("卖出") && isEnough && isInPriceRange) {
            // 1.2.1.冻结部分登记配额
            clientRegisterQuota.setAmount(clientRegisterQuota.getAmount() - groupPost.getAmount());
            // 1.2.2.更新登记配额
            clientRegisterQuotaMapper.updateById(clientRegisterQuota);
        }
        groupPostMapper.insert(groupPost);
    }

    @Override
    public DirectionPost modifyDirectionOffer(String directionPostId) {
        String operatorCode = directionPostMapper.selectById(directionPostId).getOperatorCode();
        ClientOperator clientOperator = clientOperatorMapper.selectById(operatorCode);
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientRegisterQuota clientRegisterQuota = new ClientRegisterQuota();
        //  1.判断报价交易状态
        DirectionPost directionPost = directionPostMapper.selectById(directionPostId);
        if (directionPost.getStatus().equals("已成交") || directionPost.getStatus().equals("已撤销")) {
            //  1.1.报价已成交，无法修改
            return null;
        } else if (directionPost.getStatus().equals("未成交")) {
            //  1.3.报价未成交，可以修改
            //  1.3.1.撤销原报价单，更新报价记录
            directionPost.setStatus("已撤销");
            directionPostMapper.updateById(directionPost);
            if (directionPost.getStatus().equals("买入")) {
                //  1.3.3.解冻原资金
                capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() - directionPost.getAmount() * directionPost.getPrice());
                capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() + directionPost.getAmount() * directionPost.getPrice());
                //  1.3.3.更新资金账户
                capitalAccountMapper.updateById(capitalAccount);
            } else if (directionPost.getStatus().equals("卖出")) {

                //  1.3.2.解冻原配额,冻结新配额
                //  1.3.2.解冻配额
                QueryWrapper<ClientRegisterQuota> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("client_id", client.getId()).eq("subject_matter_code", directionPost.getSubjectMatterCode());
                clientRegisterQuota = clientRegisterQuotaMapper.selectOne(queryWrapper);
                clientRegisterQuota.setAmount(clientRegisterQuota.getAmount() + directionPost.getAmount());
                //  1.3.3.更新配额账户
                clientRegisterQuotaMapper.updateById(clientRegisterQuota);
            }
        }
        return directionPost;
    }

    @Override
    public GroupPost modifyGroupOffer(String id) {

    }


    @Override
    public boolean cancelDirectionOffer(String directionPostId) {
        DirectionPost directionPost = directionPostMapper.selectById(directionPostId);
        ClientOperator clientOperator = clientOperatorMapper.selectById(directionPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        boolean isCancel = false;
        ClientRegisterQuota clientRegisterQuota = new ClientRegisterQuota();
        //  1.判断报价交易状态
        if (directionPost.getStatus().equals("已成交") || directionPost.getStatus().equals("已撤销")) {
            //  1.1.报价已成交，无法撤销
            return false;
        } else if (directionPost.getStatus().equals("未成交")) {
            //  1.3.报价未成交，可以撤销
            //  1.3.1.撤销报价
            isCancel = true;
            directionPostMapper.deleteById(directionPost.getId());
            //判断买卖方向
            if (directionPost.getFlowType().equals("买入")) {
                //  1.3.2.解冻资金
                capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() - directionPost.getAmount() * directionPost.getPrice());
                capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() + directionPost.getAmount() * directionPost.getPrice());
                //  1.3.3.更新资金账户
                capitalAccountMapper.updateById(capitalAccount);
            } else if (directionPost.getFlowType().equals("卖出")) {
                //  1.3.2.解冻配额
                QueryWrapper<ClientRegisterQuota> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("client_id", client.getId()).eq("subject_matter_code", directionPost.getSubjectMatterCode());
                clientRegisterQuota = clientRegisterQuotaMapper.selectOne(queryWrapper);
                clientRegisterQuota.setAmount(clientRegisterQuota.getAmount() + directionPost.getAmount());
                //  1.3.3.更新配额账户
                clientRegisterQuotaMapper.updateById(clientRegisterQuota);
            }
        }
        return isCancel;
    }

    @Override
    public boolean cancelGroupOffer(String groupPostId) {
        GroupPost groupPost = groupPostMapper.selectById(groupPostId);
        ClientOperator clientOperator = clientOperatorMapper.selectById(groupPost.getOperatorCode());
        Client client = clientMapper.selectById(clientOperator.getClientId());
        CapitalAccount capitalAccount = capitalAccountMapper.selectById(client.getCapitalAccountId());
        ClientRegisterQuota clientRegisterQuota = new ClientRegisterQuota();
        boolean isCancel = false;
        //  1.判断报价交易状态
        if (groupPost.getStatus().equals("已成交") || groupPost.getStatus().equals("已撤销")) {
            //  1.1.报价已成交，无法撤销
            return false;
        } else if (groupPost.getStatus().equals("未成交")) {
            //  1.3.报价未成交，可以撤销
            //  1.3.1.撤销报价
            directionPostMapper.deleteById(groupPost.getId());
            isCancel = true;
            //判断买卖方向
            if (groupPost.getFlowType().equals("买入")) {
                //  1.3.2.解冻资金
                capitalAccount.setUnavailableCapital(capitalAccount.getUnavailableCapital() - groupPost.getAmount() * groupPost.getPrice());
                capitalAccount.setAvailableCapital(capitalAccount.getAvailableCapital() + groupPost.getAmount() * groupPost.getPrice());
                //  1.3.3.更新资金账户
                capitalAccountMapper.updateById(capitalAccount);
            } else if (groupPost.getFlowType().equals("卖出")) {
                //  1.3.2.解冻配额
                QueryWrapper<ClientRegisterQuota> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("client_id", client.getId()).eq("subject_matter_code", groupPost.getSubjectMatterCode());
                clientRegisterQuota = clientRegisterQuotaMapper.selectOne(queryWrapper);
                //  1.3.3.更新配额账户
                clientRegisterQuotaMapper.updateById(clientRegisterQuota);
            }
        }
        return isCancel;
    }

    @Override
    public List selectOfferInfo(String operatorCode) {
        //  1.查询报价记录
        QueryWrapper<DirectionPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operator_code", operatorCode);
        List<DirectionPost> directionPostList = directionPostMapper.selectList(queryWrapper);
        //  2.返回报价记录
        return directionPostList;
    }

    @Override
    public List selectBargainInfo(String OperatorCode) {
        //  1.查询成交记录
        QueryWrapper<DirectionDoneRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operator_code", OperatorCode);
        List<DirectionDoneRecord> bargainList = directionDoneRecordMapper.selectList(queryWrapper);
        //  2.返回成交记录
        return bargainList;
    }
}
