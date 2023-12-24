package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 消息提示
public class Tips {
    private String id;
    private Date time;
    private String content;
    private String type; // 消息类型
    private String from; // 发出者
}