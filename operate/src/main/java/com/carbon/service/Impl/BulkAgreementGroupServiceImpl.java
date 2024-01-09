package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.mapper.ClientMapper;
import com.carbon.mapper.GroupClientMapper;
import com.carbon.mapper.GroupMapper;
import com.carbon.po.User.Client;
import com.carbon.po.BulkAgreement.Group;
import com.carbon.po.BulkAgreement.GroupClient;
import com.carbon.service.BulkAgreementGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class BulkAgreementGroupServiceImpl implements BulkAgreementGroupService {
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    GroupClientMapper groupClientMapper;
    @Override
    public List<Group> selectGroup(String clientId){
        QueryWrapper<GroupClient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id", clientId);
        List<GroupClient> groupClients = groupClientMapper.selectList(queryWrapper);

        List<Group> groups = new ArrayList<>();
        for (GroupClient groupClient : groupClients) {
            Group group = groupMapper.selectById(groupClient.getGroupId());
            groups.add(group);
        }

        return groups;
    }

    @Override
    public void addGroup(String groupName,String clientId){
        Group group=new Group();
        group.setName(groupName);
        group.setGroupMaster(clientId);
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        group.setCreateTime(timestamp);
        group.setUpdateTime(timestamp);
        groupMapper.insert(group);
        groupClientMapper.insert(new GroupClient(group.getId(),clientId));
    }
    @Override
    public void editGroup(String groupId,String newGroupName,String clientId){
        Group group=groupMapper.selectById(groupId);
        if(group.getGroupMaster().equals(clientId)){
            group.setName(newGroupName);
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            group.setUpdateTime(timestamp);
            groupMapper.updateById(group);
        }
    }
    @Override
    public void deleteGroup(String groupId,String clientId){
        Group group=groupMapper.selectById(groupId);
        if(group.getGroupMaster().equals(clientId)){
            QueryWrapper<GroupClient> groupClientQueryWrapper = new QueryWrapper<>();
            groupClientQueryWrapper.eq("group_id", groupId);
            groupClientMapper.delete(groupClientQueryWrapper);

            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            group.setUpdateTime(timestamp);
            groupMapper.updateById(group);

            QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<>();
            groupQueryWrapper.eq("id", groupId);
            groupMapper.delete(groupQueryWrapper);
        }
    }
    @Override
    public void addMember(String groupId,String memberName,String clientId){
        Group group=groupMapper.selectById(groupId);
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", memberName);
        Client member= clientMapper.selectOne(queryWrapper);
        if(group.getGroupMaster().equals(clientId)){
            GroupClient groupClient=new GroupClient(groupId,member.getId());
            groupClientMapper.insert(groupClient);

            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            group.setUpdateTime(timestamp);
            groupMapper.updateById(group);
        }
    }
    @Override
    public void deleteMember(String groupId,String memberName,String clientId){
        Group group=groupMapper.selectById(groupId);
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", memberName);
        Client member= clientMapper.selectOne(queryWrapper);
        if(group.getGroupMaster().equals(clientId)) {
            QueryWrapper<GroupClient> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("group_id", groupId).eq("client_id", member.getId());
            groupClientMapper.delete(queryWrapper1);

            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            group.setUpdateTime(timestamp);
            groupMapper.updateById(group);
        }
    }
    @Override
    public List<Client> selectGroupClient(String groupId){
        QueryWrapper<GroupClient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId);
        List<GroupClient> groupClients = groupClientMapper.selectList(queryWrapper);
        List<Client> clients = new ArrayList<>();
        for (GroupClient groupClient : groupClients) {
            Client client= clientMapper.selectById(groupClient.getClientId());
            clients.add(client);
        }
        return clients;
    }
}
