package com.carbon.service.Impl;

import com.carbon.dao.CapitalDao;
import com.carbon.input.DelistingPost;
import com.carbon.input.ListingPost;
import com.carbon.output.SelectPositionInfoResult;
import com.carbon.po.Listing;
import com.carbon.po.ListingDoneRecord;
import com.carbon.service.ListingService;

import java.util.List;

public class ListingServiceImpl implements ListingService {

    @Override
    public boolean purchaserListing(ListingPost listingPost) {
        if(CapitalDao.getAvailableCapital(listingPost.getClientId())<listingPost.getPrice()*listingPost.getAmount())
            return false;//可用资金不够
        ListingDao.addListing(listingPost);
        ListingPostDao.addListingPost(listingPost);
        return true;
    }

    @Override
    public boolean sellerListing(ListingPost listingPost) {
        if(QuotaDao.getAvailableQuota(listingPost.getClientId(),
                listingPost.getSubjectMatterCode)<listingPost.getAmount())
            return false;//可用配额不够
        ListingDao.addListing(listingPost);
        ListingPostDao.addListingPost(listingPost);
        return true;
    }

    @Override
    public boolean purchaserDelisting(DelistingPost delistingPost) {
        if(CapitalDao.getAvailableCapital(delistingPost.getClientId())<
        ListingDao.getListing(delistingPost.getListingId()).getPrice()*delistingPost.getAmount())
            return false;//可用资金不够
        ListingDao.updateListing(delistingPost);
        ListingPostDao.deleteListingPost(delistingPost);
        ListingDoneRecordDao.addListingDoneRecord(delistingPost);
        return true;
    }

    @Override
    public boolean sellerDelisting(DelistingPost delistingPost) {
        if(QuotaDao.getAvailableQuota(delistingPost.getClientId(),
                ListingDao.getListing(delistingPost.getListingId()).getSubjectMatterCode)< delistingPost.getAmount())
            return false;//可用配额不够
        ListingDao.updateListing(delistingPost);
        ListingPostDao.deleteListingPost(delistingPost);
        ListingDoneRecordDao.addListingDoneRecord(delistingPost);
        return true;
    }

    @Override
    public SelectPositionInfoResult selectPositionInfo(String clientId) {
        SelectPositionInfoResult result = new SelectPositionInfoResult();
        result.setCapitalAccount(CapitalDao.getCapital(clientId));
        result.setQuotaAccount(QuotaDao.getQuota(clientId));
        return result;
    }

    @Override
    public List<ListingPost> selectEntrustInfo(String clientId) {
        List<ListingPost> listingPostList=ListingPostDao.getListingPost(clientId);
        return listingPostList;
    }

    @Override
    public List<Listing> selectBargainInfo(String clientId) {
        List<Listing> listingList=ListingDao.getListing(clientId);
        return listingList;
    }

    @Override
    public boolean cancelListing(String listingId) {
        ListingDao.updateListing(listingId);
        ListingPostDao.deleteListingPost(listingId);
        return true;
    }

    @Override
    public void autoCancel() {
        ListingDao.updateListing();
        ListingPostDao.deleteListingPost();
        return;
    }
}
