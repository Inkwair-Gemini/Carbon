package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    GroupMapper groupMapper;
    @Autowired
    GroupClientMapper groupClientMapper;
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
        QueryWrapper<GroupClient> groupClientQueryWrapper = new QueryWrapper<>();
        groupClientQueryWrapper.eq("group_id", groupId);
        groupClientMapper.delete(groupClientQueryWrapper);

        QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<>();
        groupQueryWrapper.eq("id", groupId);
        groupMapper.delete(groupQueryWrapper);
    }
    @Override
    public void addMember(String groupId,String memberId){
        GroupClient groupClient=new GroupClient(groupId,memberId);
        groupClientMapper.insert(groupClient);
    }
    @Override
    public void deleteMember(String groupId,String memberId){
        QueryWrapper<GroupClient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId).eq("client_id", memberId);
        groupClientMapper.delete(queryWrapper);
    }
}
