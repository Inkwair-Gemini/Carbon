package com.carbon.service;

import java.util.List;

//大宗协议群组控制类
public interface BulkAgreementGroupService {
    //查询与自己相关群组
    List selectGroup(String clientId);
    //添加群组
    void addGroup(String groupName,String clientId);
    //编辑群组
    void editGroup(String groupId,String newGroupName,String clientId);
    //删除群组
    void deleteGroup(String groupId,String clientId);
    //添加成员
    void addMember(String groupId,String memberId,String clientId);
    //删除成员
    void deleteMember(String groupId,String memberId,String clientId);
}
