package com.carbon.service;

import com.carbon.po.Announcement;
import com.carbon.po.Tips;

import java.util.List;

public interface MessageService {
    //根据tips中to对象发送信息
    void sendTips(Tips tips);
    //发布公告
    void postAnnounce(Announcement announcement);
    //获取accountId的Tips列表，查阅消息
    List<Tips> getTips(String accountId);

}
