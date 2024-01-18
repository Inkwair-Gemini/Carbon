package com.carbon.service;

import com.carbon.po.Announcement;
import com.carbon.po.News;

import java.util.List;

public interface NewsService {
    //创建并绑定队列
    void createAndBindQueue(String clientId);
    //发送消息
    void sendMessage(News news);
    //发送公告
    void sendFanoutMsg(Announcement announcement);
    //获取发送的消息
    List<News> getSendedNewsList(String senderClientId);
    //获取接收的消息
    List<News> getReceivedNewsList(String receiverClientId);
    //获取公告
    List<Announcement> getAnnouncements();
}
