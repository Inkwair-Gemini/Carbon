package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

//消息
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News implements Serializable {
    private String id;
    private Timestamp time;
    private String sender;
    private String receiver;
    private String type; // 消息类型
    private String content;
}
