package com.carbon.service.Impl;

import com.carbon.po.Group;
import com.carbon.po.GroupAndMember;
import com.carbon.service.BulkAgreementGroupService;

import java.sql.Timestamp;

public class BulkAgreementGroupServiceImpl implements BulkAgreementGroupService {
    @Override
    public void addGroup(String groupName){
        Group group=new Group();
        group.setName(groupName);
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        group.setCreateTime(timestamp);
        groupDao.insertGroup(group);
    }
    @Override
    public void editGroup(String groupId,String newGroupName){
        Group group=groupDao.selectGroup(groupId);
        group.setName(newGroupName);
        groupDao.updataGroup(group);
    }
    @Override
    public void deleteGroup(String groupId){
        groupAndMemberDao.deleteGroupAndMemberByGroupId(groupId);
        groupDao.deleteGroup(groupId);
    }
    @Override
    public void addMember(String groupId,String memberId){
        GroupAndMember groupAndMember=new GroupAndMember(groupId,memberId);
        groupAndMemberDao.insertGroupAndMember(groupAndMember);
    }
    @Override
    public void deleteMember(String groupId,String memberId){
        GroupAndMember groupAndMember=new GroupAndMember(groupId,memberId);
        groupAndMemberDao.deleteGroupAndMember(groupAndMember);
    }
}