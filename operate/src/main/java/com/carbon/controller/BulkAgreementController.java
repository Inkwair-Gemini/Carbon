package com.carbon.controller;

import com.carbon.input.BulkAgreement.DirectionEnquiryPost;
import com.carbon.input.BulkAgreement.DirectionPost;
import com.carbon.input.BulkAgreement.GroupEnquiryPost;
import com.carbon.input.BulkAgreement.GroupPost;
import com.carbon.po.BulkAgreement.DirectionDoneRecord;
import com.carbon.po.BulkAgreement.Group;
import com.carbon.po.BulkAgreement.GroupDoneRecord;
import com.carbon.po.User.Client;
import com.carbon.result.Result;
import com.carbon.service.BulkAgreementEnquiryService;
import com.carbon.service.BulkAgreementGroupService;
import com.carbon.service.BulkAgreementOfferService;
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
    //接收定向报价表单directionPost
    //返回ok(boolean)
    @PostMapping("/directionOffer")
    public Result DirectionOffer(@RequestBody DirectionPost directionPost){
        try{
            boolean a=bulkAgreementOfferService.directionOffer(directionPost);
            return Result.ok(a);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //大宗协议群组报价
    //接收群组报价表单groupPost
    //返回ok(boolean)
    @PostMapping("/groupOffer")
    public Result GroupOffer(@RequestBody GroupPost groupPost){
        try{
            boolean a=bulkAgreementOfferService.groupOffer(groupPost);
            return Result.ok(a);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询大宗协议定向报价记录
    //接收操作员代码operatorCode
    //返回List<DirectionPost>
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
    //接收操作员代码operatorCode
    //返回List<GroupPost>
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
    //接收大宗协议定向报价表单ID
    //返回修改前的directionPost
    //返回值未空表示修改失败
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
    //接收群组报价表单ID
    //返回修改前的群组报价表单
    //返回未空表示修改失败
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
    //接收大宗协议定向报价表单ID
    //返回ok(boolean)
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
    //接收群组报价表单ID
    //返回ok(boolean)
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
    //接收定向洽谈表单
    //返回ok
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
    //接收群组洽谈表单
    //返回ok
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
    //接收客户号
    //返回List<DirectionDoneRecord>
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
    //接收客户号
    //返回List<GroupDoneRecord>
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
    //接收定向报价洽谈表单
    //返回ok(boolean)
    @PostMapping("/directionEnquiry")
    public Result SendDirectionEnquiry(@RequestBody DirectionEnquiryPost directionEnquiryPost){
        try{
            boolean a=bulkAgreementEnquiryService.sendDirectionOfferEnquiry(directionEnquiryPost);
            return Result.ok(a);
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //大宗协议群组报价洽谈
    //接收群组报价洽谈表单
    //返回ok(boolean)
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
    //接收操作员代码
    //返回List<DirectionEnquiryPost>
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
    //接收操作员代码
    //返回List<GroupEnquiryPost>
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
    //接收客户号
    //返回List<Group>
    @GetMapping("/selectGroup/{clientId}")
    public Result SelectGroup(@PathVariable String clientId){
        try{
            List<Group> groups= bulkAgreementGroupService.selectGroup(clientId);
            return Result.ok(groups);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //新建群组
    //接收群名与客户号
    //返回ok
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
    //接收群号、新群名、客户号
    //返回ok
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
    //接收群号、客户号
    //返回ok
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
    //接收群号、新成员姓名、客户号
    //返回ok
    @PostMapping("/addMember/{groupId}/{memberName}/{clientId}")
    public Result AddMember(@PathVariable String groupId,@PathVariable String memberName,@PathVariable String clientId){
        try{
            bulkAgreementGroupService.addMember(groupId,memberName,clientId);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //删除成员
    //接收群号、新成员姓名、客户号
    //返回ok
    @PostMapping("/deleteMember/{groupId}/{memberName}/{clientId}")
    public Result DeleteMember(@PathVariable String groupId,@PathVariable String memberName,@PathVariable String clientId){
        try{
            bulkAgreementGroupService.deleteMember(groupId,memberName,clientId);
            return Result.ok();
        }catch(Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询群组内成员信息
    //接收群号
    //返回List<Group>
    @GetMapping("/selectClient/{groupId}")
    public Result SelectClient(@PathVariable String groupId){
        try{
            List<Client> clients=bulkAgreementGroupService.selectGroupClient(groupId);
            return Result.ok(clients);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询大宗协议定向报价记录
    @GetMapping("/selectDirectionOfferInfo/{operatorCode}")
    public Result selectDirectionOfferInfo(@PathVariable String operatorCode) {
        try {
            List<DirectionPost> DirectionPosts = bulkAgreementOfferService.selectDirectionOfferInfo(operatorCode);
            return Result.ok(DirectionPosts);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询大宗协议群组报价记录
    @GetMapping("/selectGroupOfferInfo/{operatorCode}")
    public Result selectGroupOfferInfo(@PathVariable String operatorCode) {
        try {
            List<GroupPost> GroupPosts = bulkAgreementOfferService.selectGroupOfferInfo(operatorCode);
            return Result.ok(GroupPosts);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询大宗协议定向询价记录
    @GetMapping("/selectDirectionOfferEnquiry/{operatorCode}")
    public Result selectDirectionOfferEnquiry(@PathVariable String operatorCode) {
        try {
            List<DirectionEnquiryPost> DirectionEnquiryPosts = bulkAgreementEnquiryService.selectDirectionOfferEnquiry(operatorCode);
            return Result.ok(DirectionEnquiryPosts);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询大宗协议定向询价记录
    @GetMapping("/selectGroupOfferEnquiry/{operatorCode}")
    public Result selectGroupOfferEnquiry(@PathVariable String operatorCode) {
        try {
            List<GroupEnquiryPost> GroupPosts = bulkAgreementEnquiryService.selectGroupOfferEnquiry(operatorCode);
            return Result.ok(GroupPosts);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询大宗协议当日定向报价记录
    @GetMapping("/selectDayDirectionOfferInfo/{clientId}")
    public Result selectDayDirectionOfferInfo(@PathVariable String clientId) {
        try {
            List<DirectionPost> DirectionPosts = bulkAgreementOfferService.selectDayDirectionOfferInfo(clientId);
            return Result.ok(DirectionPosts);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询大宗协议群组报价记录
    @GetMapping("/selectDayGroupOfferInfo/{clientId}")
    public Result selectDayGroupOfferInfo(@PathVariable String clientId) {
        try {
            List<GroupPost> GroupPosts = bulkAgreementOfferService.selectDayGroupOfferInfo(clientId);
            return Result.ok(GroupPosts);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询大宗协议当日定向报价成交记录
    @GetMapping("/selectDayDirectionDoneRecord/{clientId}")
    public Result SelectDayDirectionDoneRecord(@PathVariable String clientId){
        try{
            List<DirectionDoneRecord> directionDoneRecords=bulkAgreementOfferService.selectDayDirectionBargainInfo(clientId);
            return Result.ok(directionDoneRecords);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    //查询大宗协议群组报价当日成交记录
    @GetMapping("/selectDayGroupDoneRecord/{clientId}")
    public Result SelectDayGroupDoneRecord(@PathVariable String clientId){
        try{
            List<GroupDoneRecord> groupDoneRecords=bulkAgreementOfferService.selectDayGroupBargainInfo(clientId);
            return Result.ok(groupDoneRecords);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询大宗协议当日定向询价记录
    @GetMapping("/selectDayDirectionOfferEnquiry/{clientId}")
    public Result selectDayDirectionOfferEnquiry(@PathVariable String clientId) {
        try {
            List<DirectionEnquiryPost> DirectionEnquiryPosts = bulkAgreementEnquiryService.selectDayDirectionOfferEnquiry(clientId);
            return Result.ok(DirectionEnquiryPosts);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
    //查询大宗协议当日定向询价记录
    @GetMapping("/selectDayGroupOfferEnquiry/{clientId}")
    public Result selectDayGroupOfferEnquiry(@PathVariable String clientId) {
        try {
            List<GroupEnquiryPost> GroupPosts = bulkAgreementEnquiryService.selectDayGroupOfferEnquiry(clientId);
            return Result.ok(GroupPosts);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
}

