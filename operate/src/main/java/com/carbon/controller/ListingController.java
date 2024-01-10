package com.carbon.controller;

import com.carbon.input.Auction.AuctionPost;
import com.carbon.input.Listing.DelistingPost;
import com.carbon.input.Listing.ListingPost;
import com.carbon.po.Auction.AuctionDoneRecord;
import com.carbon.po.Listing.ListingDoneRecord;
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
    //传入客户Id，标的物代码，标的物名称，账户类型，库存账号，委托价格，委托数量，操作员代码
    //返回ok
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
    //传入客户Id，标的物代码，标的物名称，账户类型，库存账号，委托价格，委托数量，操作员代码
    //返回ok
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
    //传入客户Id，挂牌表单Id，账户类型，库存账号，摘牌数量，操作员代码
    //返回ok
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
    //传入客户Id，挂牌表单Id，账户类型，库存账号，摘牌数量，操作员代码
    //返回ok
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
    //传入挂牌表单Id
    //返回ok
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

//    // 持仓查询
//    //传入客户Id
//    //返回SelectPositionInfoResult
//    @GetMapping("/positionInfo/{clientId}")
//    public Result SelectPositionInfo(@PathVariable String clientId){
//        try {
//            SelectPositionInfoResult result = ListingService.selectPositionInfo(clientId);
//            return Result.ok(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            return Result.fail();
//        }
//    }

    // 委托查询
    //传入客户Id
    //返回List<ListingPost>
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

    // 历史委托查询
    //传入客户Id，开始时间，结束时间
    //返回List<ListingPost>
    @PostMapping("history_entrustInfo")
    public Result SelectEntrustInfo(@RequestBody String clientId,@RequestBody Timestamp start,@RequestBody Timestamp end){
        try {
            List<ListingPost> listingPostList = ListingService.selectEntrustInfo(clientId,start,end);
            return Result.ok(listingPostList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 成交查询
    //传入客户Id
    //返回List<ListingDoneRecord>
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

    // 历史成交查询
    //传入客户Id，开始时间，结束时间
    //返回List<ListingDoneRecord>
    @PostMapping("/history_bargainInfo")
    public Result SelectBargainInfo(@RequestBody String clientId,@RequestBody Timestamp start,@RequestBody Timestamp end){
        try {
            List<ListingDoneRecord> listingDoneList = ListingService.selectBargainInfo(clientId,start,end);
            return Result.ok(listingDoneList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 查询当前买方挂牌
    //返回List<ListingPost>
    @GetMapping("/selectPurchaser")
    public Result SelectPurchaserListing(){
        try {
            List<ListingPost> listingPostList = ListingService.selectPurchaserListing();
            return Result.ok(listingPostList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    // 查询当前卖方挂牌
    //返回List<ListingPost>
    @GetMapping("/selectSeller")
    public Result SelectSellerListing(){
        try {
            List<ListingPost> listingPostList = ListingService.selectSellerListing();
            return Result.ok(listingPostList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询当日挂牌交易委托记录
    @GetMapping("/selectDayListingPost/{clientId}")
    public Result selectDayListingPost(@PathVariable String clientId){
        try{
            List<ListingPost> listingPosts = ListingService.selectDayListingPost(clientId);
            return Result.ok(listingPosts);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询当日挂牌交易成交记录
    @GetMapping("/selectDayListingDoneRecord/{clientId}")
    public Result selectDayListingDoneRecord(@PathVariable String clientId){
        try{
            List<ListingDoneRecord> listingDoneRecords = ListingService.selectDayListingDoneRecord(clientId);
            return Result.ok(listingDoneRecords);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
}
