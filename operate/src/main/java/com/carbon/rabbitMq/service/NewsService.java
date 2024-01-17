package com.carbon.rabbitMq.service;

import com.carbon.rabbitMq.model.Announcement;
import com.carbon.rabbitMq.model.News;

public interface NewsService {
    //创建并绑定队列
    void createAndBindQueue(String clientId);
    //发送消息
    void sendMessage(News news);
    //发送公告
    void sendFanoutMsg(Announcement announcement);
}
