package com.carbon.controller;

import com.carbon.po.Announcement;
import com.carbon.po.News;
import com.carbon.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/createAndBindQueue/{clientId}")
    public void createAndBindQueue(@PathVariable String clientId){
        newsService.createAndBindQueue(clientId);
    }

    @PostMapping("/sendAnnouncement")
    public String sendAnnouncement(@RequestBody Announcement announcement){
        newsService.sendFanoutMsg(announcement);
        return "ok";
    }

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody News news){
        newsService.sendMessage(news);
    }

    @GetMapping("/getAnnouncements")
    public List getAnnouncements(){
        return newsService.getAnnouncements();
    }

    @GetMapping("/getNews/{clientId}")
    public List getNews(@PathVariable String clientId){
        List<News> sendedNewsList = newsService.getSendedNewsList(clientId);
        List<News> receivedNewsList = newsService.getReceivedNewsList(clientId);
        sendedNewsList.addAll(receivedNewsList);
        return sendedNewsList;
    }
}
