package com.carbon.service.Impl;

import com.carbon.dao.CapitalDao;
import com.carbon.input.DirectionPost;
import com.carbon.input.GroupPost;
import com.carbon.input.ListingPost;
import com.carbon.po.CapitalAccount;
import com.carbon.po.ClientOperator;
import com.carbon.po.Group;
import com.carbon.po.QuotaAccount;
import com.carbon.service.BulkAgreementOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ClientOperatorDao clientOperatorDao;
    @Autowired
    private CapitalDao capitalDao;
    @Autowired
    private QuotaDao quotaDao;

    @Override
    public void directionOffer(DirectionPost directionPost) {
        ClientOperator clientOperator = clientOperatorDao.selectClientOperatorById(String clientOperatorId);
        CapitalAccount capitalAccount = capitalDao.selectCapitalAccount(clientOperator.getAccountId());
        QuotaAccount quotaAccount = quotaDao.selectQuotaAccount(clientOperator.getAccountId());
        //todo 1.判断是否有足够的配额
        boolean isEnoughQuota = quotaAccount.getQuota() >= directionPost.getQuota();
        //todo 2.判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getCapital() >= directionPost.getCapital();
        //todo 3.提交定向报价
        if (isEnoughQuota && isEnoughCapital) {
            //todo 3.1.冻结部分配额
            quotaAccount.setQuota(quotaAccount.getQuota() - directionPost.getQuota());
            //todo 3.2.冻结部分资金
            capitalAccount.setCapital(capitalAccount.getCapital() - directionPost.getCapital());
            //todo 3.3.更新配额账户
            quotaDao.updateQuotaAccount(quotaAccount);
            //todo 3.4.更新资金账户
            capitalDao.updateCapitalAccount(capitalAccount);
            //todo 3.5.提交定向报价
            //todo 3.6.更新定向报价记录
        }
    }

    @Override
    public void groupOffer(GroupPost groupPost) {
        ClientOperator clientOperator = clientOperatorDao.selectClientOperatorById(String clientOperatorId);
        CapitalAccount capitalAccount = capitalDao.selectCapitalAccount(clientOperator.getAccountId());
        QuotaAccount quotaAccount = quotaDao.selectQuotaAccount(clientOperator.getAccountId());
        //todo 1.判断是否有足够的配额
        boolean isEnoughQuota = quotaAccount.getQuota() >= groupPost.getQuota();
        //todo 2.判断是否有足够的资金
        boolean isEnoughCapital = capitalAccount.getCapital() >= groupPost.getCapital();

    }

    @Override
    public void modifyOffer() {
        //todo 1.判断报价交易状态
        DirectionPost directionPost = directionPostDao.selectDirectionPostById(String directionPostId);
        if (directionPost.getStatus().equals("已成交")) {
            //todo 1.1.报价已成交，无法修改
        } else if (directionPost.getStatus().equals("已撤销")) {
            //todo 1.2.报价已撤销，无法修改
        } else {
            //todo 1.3.报价未成交，可以修改
            //todo 1.3.1.撤销原报价单，生成新报价单
            directionPostDao.deleteDirectionPost(String directionPostId);
            directionPostDao.insertDirectionPost(directionPost);
            //todo 1.3.2.解冻原配额,冻结新配额
            QuotaAccount quotaAccount = quotaDao.selectQuotaAccount(directionPost.getAccountId());
            quotaAccount.setQuota(quotaAccount.getQuota() + directionPost.getQuota());
            quotaAccount.setQuota(quotaAccount.getQuota() - NewDirectionPost.getQuota());
            //todo 1.3.3.解冻原资金,冻结新资金
            CapitalAccount capitalAccount = capitalDao.selectCapitalAccount(directionPost.getAccountId());
            capitalAccount.setCapital(capitalAccount.getCapital() + directionPost.getCapital());
            capitalAccount.setCapital(capitalAccount.getCapital() - NewDirectionPost.getCapital());
        }
    }

    @Override
    public void cancelOffer() {
        //todo 1.判断报价交易状态
        DirectionPost directionPost = directionPostDao.selectDirectionPostById(String directionPostId);
        if (directionPost.getStatus().equals("已成交")) {
            //todo 1.1.报价已成交，无法撤销
        } else if (directionPost.getStatus().equals("已撤销")) {
            //todo 1.2.报价已撤销，无法撤销
        } else {
            //todo 1.3.报价未成交，可以撤销
            //todo 1.3.1.撤销报价
            directionPostDao.deleteDirectionPost(String directionPostId);
            //todo 1.3.2.解冻配额
            QuotaAccount quotaAccount = quotaDao.selectQuotaAccount(directionPost.getAccountId());
            quotaAccount.setQuota(quotaAccount.getQuota() + directionPost.getQuota());
            //todo 1.3.3.解冻资金
            CapitalAccount capitalAccount = capitalDao.selectCapitalAccount(directionPost.getAccountId());
            capitalAccount.setCapital(capitalAccount.getCapital() + directionPost.getCapital());
        }

    }

    @Override
    public List selectOfferInfo() {
        //todo 1.查询报价记录
        List<DirectionPost> directionPostList = directionPostDao.selectDirectionPost();
        //todo 2.返回报价记录
        return directionPostList;
    }

    @Override
    public List selectBargainInfo() {
        //todo 1.查询成交记录
        List<Bargain> bargainList = bargainDao.selectBargain();
        //todo 2.返回成交记录
        return bargainList;
    }
}
