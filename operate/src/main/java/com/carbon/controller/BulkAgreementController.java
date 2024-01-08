package com.carbon.controller;

import com.carbon.input.DirectionEnquiryPost;
import com.carbon.input.DirectionPost;
import com.carbon.input.GroupEnquiryPost;
import com.carbon.input.GroupPost;
import com.carbon.mapper.GroupClientMapper;
import com.carbon.mapper.GroupMapper;
import com.carbon.po.DirectionDoneRecord;
import com.carbon.po.Group;
import com.carbon.po.GroupDoneRecord;
import com.carbon.result.Result;
import com.carbon.service.BulkAgreementEnquiryService;
import com.carbon.service.BulkAgreementGroupService;
import com.carbon.service.BulkAgreementOfferService;
import com.carbon.service.TradeSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bulkAgreement")
public class BulkAgreementController {
    @Autowired
    BulkAgreementGroupService bulkAgreementGroupService;
    @Autowired
    BulkAgreementOfferService bulkAgreementOfferService;
    @Autowired
    BulkAgreementEnquiryService bulkAgreementEnquiryService;

    //大宗协议定向报价
    @PostMapping("/directionOffer")
    public Result DirectionOffer(@RequestBody DirectionPost directionPost){
        try{
            bulkAgreementOfferService.directionOffer(directionPost);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //大宗协议群组报价
    @PostMapping("/groupOffer")
    public Result GroupOffer(@RequestBody GroupPost groupPost){
        try{
            bulkAgreementOfferService.groupOffer(groupPost);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询大宗协议定向报价记录
    @GetMapping("/selectDirectionOffer/{operatorCode}")
    public Result SelectDirectionOffer(@PathVariable String operatorCode){
        try{
            List<DirectionPost> directionPosts=bulkAgreementOfferService.selectDirectionOfferInfo(operatorCode);
            return Result.ok(directionPosts);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询大宗协议群组报价记录
    @GetMapping("/selectGroupOffer/{operatorCode}")
    public Result SelectGroupOffer(@PathVariable String operatorCode){
        try{
            List<GroupPost> groupPosts=bulkAgreementOfferService.selectGroupOfferInfo(operatorCode);
            return Result.ok(groupPosts);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //修改大宗协议定向报价信息
    @PostMapping("/modifyDirectionOffer/{directionPostId}")
    public Result ModifyDirectionOffer(@PathVariable String directionPostId){
        try{
            DirectionPost directionPost1 = bulkAgreementOfferService.modifyDirectionOffer(directionPostId);
            return  Result.ok(directionPost1);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //修改大宗协议群组报价信息
    @PostMapping("/modifyGroupOffer/{groupPostId}")
    public Result ModifyGroupOffer(@PathVariable String groupPostId){
        try{
            GroupPost groupPost1 = bulkAgreementOfferService.modifyGroupOffer(groupPostId);
            return  Result.ok(groupPost1);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //撤销大宗协议定向报价信息
    @PostMapping("/deleteDirectionOffer/{directionPostId}")
    public Result DeleteDirectionOffer(@PathVariable String directionPostId){
        try{
            boolean a = bulkAgreementOfferService.cancelDirectionOffer(directionPostId);
            return  Result.ok(a);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //撤销大宗协议群组报价信息
    @PostMapping("/deleteGroupOffer/{groupPostId}")
    public Result DeleteGroupOffer(@PathVariable String groupPostId){
        try{
            boolean a = bulkAgreementOfferService.cancelGroupOffer(groupPostId);
            return  Result.ok(a);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //大宗协议定向报价成交
    @PostMapping("/directionDone")
    public Result MakeDirectionBargain(@RequestBody DirectionEnquiryPost directionEnquiryPost){
        try{
            bulkAgreementEnquiryService.makeDirectionBargain(directionEnquiryPost);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //大宗协议群组报价成交
    @PostMapping("/groupDone")
    public Result MakeGroupBargain(@RequestBody GroupEnquiryPost groupEnquiryPost){
        try{
            bulkAgreementEnquiryService.makeGroupBargain(groupEnquiryPost);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询大宗协议定向报价成交记录
    @GetMapping("/selectDirectionDoneRecord/{clientId}")
    public Result SelectDirectionDoneRecord(@PathVariable String clientId){
        try{
            List<DirectionDoneRecord> directionDoneRecords=bulkAgreementOfferService.selectDirectionBargainInfo(clientId);
            return Result.ok(directionDoneRecords);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询大宗协议群组报价成交记录
    @GetMapping("/selectGroupDoneRecord/{clientId}")
    public Result SelectGroupDoneRecord(@PathVariable String clientId){
        try{
            List<GroupDoneRecord> groupDoneRecords=bulkAgreementOfferService.selectGroupBargainInfo(clientId);
            return Result.ok(groupDoneRecords);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //大宗协议定向报价洽谈
    @PostMapping("/directionEnquiry")
    public Result SendDirectionEnquiry(@RequestBody DirectionEnquiryPost directionEnquiryPost){
        try{
            bulkAgreementEnquiryService.sendDirectionOfferEnquiry(directionEnquiryPost);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //大宗协议群组报价洽谈
    @PostMapping("/groupEnquiry")
    public Result SendGroupEnquiry(@RequestBody GroupEnquiryPost groupEnquiryPost){
        try{
            bulkAgreementEnquiryService.sendGroupOfferEnquiry(groupEnquiryPost);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询大宗协议定向报价洽谈
    @GetMapping("/selectDirectionEnquiry/{operatorCode}")
    public Result SelectDirectionEnquiry(@PathVariable String operatorCode){
        try{
            List<DirectionEnquiryPost> directionEnquiryPosts = bulkAgreementEnquiryService.selectDirectionOfferEnquiry(operatorCode);
            return Result.ok(directionEnquiryPosts);
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询大宗协议群组报价洽谈
    @PostMapping("/selectGroupEnquiry/{operatorCode}")
    public Result SelectGroupEnquiry(@PathVariable String operatorCode){
        try{
            List<GroupEnquiryPost> groupEnquiryPosts = bulkAgreementEnquiryService.selectGroupOfferEnquiry(operatorCode);
            return Result.ok(groupEnquiryPosts);
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询与自己相关的群组
    @GetMapping("/selectGroup/{clientId}")
    public Result SelectGroup(@PathVariable String clientId){
        try{
            List<Group> groups=bulkAgreementGroupService.selectGroup(clientId);
            return Result.ok(groups);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //新建群组
    @PostMapping("/createGroup/{groupName}/{clientId}")
    public Result CreateGroup(@PathVariable String groupName,@PathVariable String clientId){
        try{
            bulkAgreementGroupService.addGroup(groupName,clientId);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //编辑群组
    @PostMapping("/modifyGroup/{groupId}/{newGroupName}/{clientId}")
    public Result ModifyGroup(@PathVariable String groupId,@PathVariable String newGroupName,@PathVariable String clientId){
        try{
            bulkAgreementGroupService.editGroup(groupId,newGroupName,clientId);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //删除群组
    @PostMapping("/deleteGroup/{groupId}/{clientId}")
    public Result DeleteGroup(@PathVariable String groupId,@PathVariable String clientId){
        try{
            bulkAgreementGroupService.deleteGroup(groupId,clientId);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //添加成员
    @PostMapping("/addMember/{groupId}/{memberId}/{clientId}")
    public Result AddMember(@PathVariable String groupId,@PathVariable String memberId,@PathVariable String clientId){
        try{
            bulkAgreementGroupService.addMember(groupId,memberId,clientId);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //删除成员
    @PostMapping("/deleteMember/{groupId}/{memberId}/{clientId}")
    public Result DeleteMember(@PathVariable String groupId,@PathVariable String memberId,@PathVariable String clientId){
        try{
            bulkAgreementGroupService.deleteMember(groupId,memberId,clientId);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
}

