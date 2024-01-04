package com.carbon.service.Impl;

import com.carbon.mapper.GroupClientMapper;
import com.carbon.mapper.GroupMapper;
import com.carbon.po.Group;
import com.carbon.po.GroupClient;
import com.carbon.service.BulkAgreementGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
@Service

public class BulkAgreementGroupServiceImpl implements BulkAgreementGroupService {
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupClientMapper groupClientMapper;
    @Override
    public void addGroup(String groupName){
        Group group=new Group();
        group.setName(groupName);
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        group.setCreateTime(timestamp);
        groupMapper.insert(group);
    }
    @Override
    public void editGroup(String groupId,String newGroupName){
        Group group=groupMapper.selectById(groupId);
        group.setName(newGroupName);
        groupMapper.updateById(group);
    }
    @Override
    public void deleteGroup(String groupId){
        groupClientMapper.deleteById(groupId);
        groupDao.deleteGroup(groupId);
    }
    @Override
    public void addMember(String groupId,String memberId){
        GroupClient groupClient=new GroupClient(groupId,memberId);
        groupClientDao.insertGroupClient(groupClient);
    }
    @Override
    public void deleteMember(String groupId,String memberId){
        GroupClient groupClient=new GroupClient(groupId,memberId);
        groupClientDao.deleteGroupClient(groupClient);
    }
}
