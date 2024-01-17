package com.carbon.rabbitMq.controller;

import com.carbon.rabbitMq.model.Announcement;
import com.carbon.rabbitMq.model.News;
import com.carbon.rabbitMq.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/createAndBindQueue/{clientId}")
    public void createAndBindQueue(@PathVariable String clientId){
        newsService.createAndBindQueue(clientId);
    }

    @PostMapping("/sendAnnouncement")
    public String sendAnnouncement(@RequestBody Announcement announcement){
        // 1.todo 存储公告
        // 2.发布公告
        newsService.sendFanoutMsg(announcement);
        return "ok";
    }
    @GetMapping("/getAnnouncements")
    public List getAnnouncements(){
        List<Announcement> list = new ArrayList<>();
        Announcement announcement = new Announcement();
        announcement.setId("2131231");
        announcement.setTime(LocalDateTime.now().toString());
        announcement.setTitle("数据库公告");
        announcement.setContext("1242143132dqasfasd 12 dwqdawdaw");
        list.add(announcement);
        return list;
    }
    //以下内容在前端也可实现
    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody News news){
        newsService.sendMessage(news);
    }
}
