package com.carbon.dbTest;

import com.carbon.input.Auction.AuctionPost;
import com.carbon.input.CapitalAndQuota.QuotaTransferPost;
import com.carbon.mapper.*;
import com.carbon.po.Auction.AuctionDoneRecord;
import com.carbon.po.Auction.AuctionQuota;
import com.carbon.po.User.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class dbTest {
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    QuotaTransferPostMapper quotaPostMapper;
    @Autowired
    AuctionDoneRecordMapper auctionDoneRecordMapper;
    @Autowired
    AuctionPostMapper auctionPostMapper;
    @Autowired
    AuctionQuotaMapper auctionQuotaMapper;
    @Test
    public void test(){
        for (int i = 0; i <1;i++) {
//            auctionDoneRecordMapper.insert(new AuctionDoneRecord(null,(new Timestamp(new Date().getTime())),"ZHOU","测试标的物4",i*100.0,300.0*i,"000002","000003"));
//            auctionPostMapper.insert(new AuctionPost(null, (new Timestamp(new Date().getTime())), "INK", "测试标的物1", 20.0 + i * 1.0, "0001"));
            auctionQuotaMapper.insert(new AuctionQuota(null, (new Timestamp(new Date().getTime())), "INK", "测试标的物1", 20.0 + i * 1.0,20.0+i*3.0,(20.0 + i * 1.0)*(20.0+i*3.0),21.0,"000002","进行中"));
        }
    }
}