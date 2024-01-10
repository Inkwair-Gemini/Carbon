package com.carbon.controller;

import com.carbon.input.Auction.AuctionPost;
import com.carbon.input.Auction.AuctionRequest;
import com.carbon.po.Auction.AuctionQuota;
import com.carbon.result.Result;
import com.carbon.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class AuctionController {
    @Autowired
    AuctionService auctionService;

    //申请拍卖物品
    //接收单向竞价拍卖申请表单
    //返回ok
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

    //查询当天所有单向竞价商品
    //无接收
    //返回List<AuctionQuota>
    @GetMapping("/selectAuctionQuota")
    public Result SelectAuctionQuota(){
        try{
            List<AuctionQuota> auctionQuotas=auctionService.selectAuctionQuota();
            return Result.ok(auctionQuotas);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //参加拍卖
    //接收单向竞价商品ID、客户操作员号
    //返回ok
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
    //接收单向竞价商品号、客户操作员号
    //返回ok
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
    //接收单向竞价表单
    //返回ok
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
    //接收单向竞价商品ID
    //返回List<AuctionPost>
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
    //接收单项竞价商品信息
    //返回ok(boolean)
    @PostMapping("/finishPay")
    public Result FinishPay(@RequestBody AuctionQuota auctionQuota){
        try{
            boolean a = auctionService.finishPay(auctionQuota);
            return Result.ok(a);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
}
