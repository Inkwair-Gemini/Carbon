package com.carbon.service;

//大宗协议群组控制类
public interface BulkAgreementGroupService {
    //添加群组
    void addGroup(String groupName);
    //编辑群组
    void editGroup(String groupId,String newGroupName);
    //删除群组
    void deleteGroup(String groupId);
    //添加成员
    void addMember(String groupId,String memberId);
    //删除成员
    void deleteMember(String groupId,String memberId);
}
