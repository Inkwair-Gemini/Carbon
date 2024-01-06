package com.carbon.controller;

import com.carbon.input.AuctionPost;
import com.carbon.input.AuctionRequest;
import com.carbon.po.AuctionQuota;
import com.carbon.result.Result;
import com.carbon.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class AuctionController {
    @Autowired
    AuctionService auctionService;

    //申请拍卖物品
    @PostMapping("/requestAuction")
    public Result RequestAuction(@RequestBody AuctionRequest auctionRequest){
        try{
            auctionService.requestAuction(auctionRequest);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //参加拍卖
    @PostMapping("/joinAuction/{auctionQuotaId}/{clientOperatorCode}")
    public Result JoinAuction(@PathVariable String auctionQuotaId,@PathVariable String clientOperatorCode){
        try{
            auctionService.joinAuction(auctionQuotaId,clientOperatorCode);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //离开拍卖
    @PostMapping("/leaveAuction/{auctionQuotaId}/{clientOperatorCode}")
    public Result LeaveAuction(@PathVariable String auctionQuotaId,@PathVariable String clientOperatorCode){
        try{
            auctionService.leaveAuction(auctionQuotaId,clientOperatorCode);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //提交竞价
    @PostMapping("/submitOffer")
    public Result SubmitOffer(@RequestBody AuctionPost auctionPost){
        try{
            auctionService.submitOffer(auctionPost);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询竞价记录
    @GetMapping("/selectOffer/{auctionQuotaId}")
    public Result SelectOffer(@PathVariable String auctionQuotaId){
        try{
             List<AuctionPost> auctionPosts = auctionService.selectOffer(auctionQuotaId);
            return Result.ok(auctionPosts);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //完成单向竞价
    @PostMapping("/finishPay")
    public Result FinishPay(@RequestBody AuctionQuota auctionQuota){
        try{
            //判断是否有人出价
            List<AuctionPost> auctionPosts=auctionService.selectOffer(auctionQuota.getId());
            if(auctionPosts.size()!=0){//有人出价
                //寻找出价最高的报价单
                AuctionPost highAuctionPost = auctionService.selectHighestOffer(auctionPosts);
                auctionService.finishPay(auctionQuota,highAuctionPost);
            }
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
}
