package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 消息提示
public class Tips implements Serializable {
    private String id;
    private Timestamp time;
    private String content;
    private String type; // 消息类型
    private String from; // 发出者
    private String to; // 接收者
}