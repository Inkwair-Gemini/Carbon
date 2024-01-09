package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carbon.mapper.AnnouncementMapper;
import com.carbon.mapper.TipsMapper;
import com.carbon.po.Tips;
import com.carbon.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    TipsMapper tipsMapper;
    @Autowired
    AnnouncementMapper announcementMapper;
    @Override
    public void sendTips(Tips tips) {
        tipsMapper.insert(tips);
    }
//    @Override
//    public void postAnnounce(Announcement announcement) {
//        announcementMapper.insert(announcement);
//    }
    @Override
    public List<Tips> getTips(String accountId){
        QueryWrapper<Tips> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_user", accountId).or().eq("to_user", accountId);
        return tipsMapper.selectList(queryWrapper);
    }
}
