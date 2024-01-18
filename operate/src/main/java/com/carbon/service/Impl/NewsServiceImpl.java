package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.mapper.AnnouncementMapper;
import com.carbon.mapper.NewsMapper;
import com.carbon.po.Announcement;
import com.carbon.po.News;
import com.carbon.service.NewsService;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//1.此交换机名不存在，则declareExchange会创建并使用该交换机，若该交换机已存在，直接使用该交换机，队列或绑定关系同理
//2.创建交换机，队列以及绑定关系都需要RabbitAdmin进行declare才会正确的存储到rabbitmq中

@Service
public class NewsServiceImpl implements NewsService {
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    RabbitAdmin rabbitAdmin;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    AnnouncementMapper announcementMapper;

    private final static String announcementExchange = "AnnouncementFanoutExchange";
    private final static String newsExchange = "NewsFanoutExchange";

    @Override
    public List<News> getSendedNewsList(String senderClientId) {
        QueryWrapper<News> query = new QueryWrapper<News>();
        query.eq("sender",senderClientId);
        return newsMapper.selectList(query);
    }
    @Override
    public List<News> getReceivedNewsList(String receiverClientId) {
        QueryWrapper<News> query = new QueryWrapper<News>();
        query.eq("receiver",receiverClientId);
        return newsMapper.selectList(query);
    }

    @Override
    public List<Announcement> getAnnouncements() {
        return announcementMapper.selectList(null);
    }

    @Override
    public void sendFanoutMsg(Announcement announcement){
        announcementMapper.insert(announcement);
        rabbitTemplate.convertAndSend(announcementExchange, null, announcement);
    }

    @Override
    public void sendMessage(News news){
        newsMapper.insert(news);
        rabbitTemplate.convertAndSend(newsExchange, null, news);
    }
    @Override
    public void createAndBindQueue(String clientId) {
        FanoutExchange exchange1 = ExchangeBuilder.fanoutExchange(announcementExchange).durable(true).build();
        rabbitAdmin.declareExchange(exchange1);

        FanoutExchange exchange2 = ExchangeBuilder.fanoutExchange(newsExchange).durable(true).build();
        rabbitAdmin.declareExchange(exchange2);

        Queue newsqueue = new Queue("news" + clientId);
        rabbitAdmin.declareQueue(newsqueue);

        Queue announcementqueue = new Queue("announcement" + clientId);
        rabbitAdmin.declareQueue(announcementqueue);

        Binding bind1 = BindingBuilder.bind(announcementqueue).to(exchange1);
        rabbitAdmin.declareBinding(bind1);

        Binding bind2 = BindingBuilder.bind(newsqueue).to(exchange2);
        rabbitAdmin.declareBinding(bind2);
    }
}
