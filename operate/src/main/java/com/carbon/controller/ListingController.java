package com.carbon.controller;

import com.carbon.input.DelistingPost;
import com.carbon.input.ListingPost;
import com.carbon.output.SelectPositionInfoResult;
import com.carbon.po.ListingDoneRecord;
import com.carbon.result.Result;
import com.carbon.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/listing")
public class ListingController {
    @Autowired
    ListingService ListingService;

    // 买方挂牌
    @PostMapping("/purchaser")
    public Result ListingPurchaser(@RequestBody ListingPost listingPost){
        listingPost.setTime(new Timestamp(System.currentTimeMillis()));
        listingPost.setFlowType("买入");
        listingPost.setListingType("挂牌");
        listingPost.setStatus("待交易");
        try {
            if(ListingService.purchaserListing(listingPost))
                return Result.ok();
            else
                return Result.fail();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 卖方挂牌
    @PostMapping("/seller")
    public Result ListingSeller(@RequestBody ListingPost listingPost){
        listingPost.setTime(new Timestamp(System.currentTimeMillis()));
        listingPost.setFlowType("卖出");
        listingPost.setListingType("挂牌");
        listingPost.setStatus("待交易");
        try {
            if(ListingService.sellerListing(listingPost))
                return Result.ok();
            else
                return Result.fail();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 买方摘牌
    @PostMapping("/depurchaser")
    public Result DelistingPurchaser(@RequestBody DelistingPost delistingPost){
        delistingPost.setTime(new Timestamp(System.currentTimeMillis()));
        delistingPost.setFlowType("买入");
        delistingPost.setListingType("摘牌");
        try {
            if(ListingService.purchaserDelisting(delistingPost))
                return Result.ok();
            else
                return Result.fail();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 卖方摘牌
    @PostMapping("/deseller")
    public Result DelistingSeller(@RequestBody DelistingPost delistingPost){
        delistingPost.setTime(new Timestamp(System.currentTimeMillis()));
        delistingPost.setFlowType("卖出");
        delistingPost.setListingType("摘牌");
        try {
            if(ListingService.sellerDelisting(delistingPost))
                return Result.ok();
            else
                return Result.fail();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 挂牌撤销
    @PostMapping("/cancel")
    public Result CancelListing(@RequestBody String listingId){
        try {
            if(ListingService.cancelListing(listingId))
                return Result.ok();
            else
                return Result.fail();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 持仓查询
    @GetMapping("/positionInfo/{clientId}")
    public Result SelectPositionInfo(@PathVariable String clientId){
        try {
            SelectPositionInfoResult result = ListingService.selectPositionInfo(clientId);
            return Result.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 委托查询
    @GetMapping("/entrustInfo/{clientId}")
    public Result SelectEntrustInfo(@PathVariable String clientId){
        try {
            List<ListingPost> listingPostList = ListingService.selectEntrustInfo(clientId);
            return Result.ok(listingPostList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 成交查询
    @GetMapping("/bargainInfo/{clientId}")
    public Result SelectBargainInfo(@PathVariable String clientId){
        try {
            List<ListingDoneRecord> listingDoneList = ListingService.selectBargainInfo(clientId);
            return Result.ok(listingDoneList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

}
