package com.carbon.service.Impl;

import com.carbon.dao.MessageDao;
import com.carbon.po.Announcement;
import com.carbon.po.Tips;
import com.carbon.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageDao messageDao;
    @Override
    public void sendTips(Tips tips) {
        messageDao.insertTips(tips.getTo());
    }
    @Override
    public void postAnnounce(Announcement announcement) {
        messageDao.insertAnnouncement();
    }
    @Override
    public List<Tips> getTips(String accountId){
        return messageDao.selectTipsById(accountId);
    }
}
