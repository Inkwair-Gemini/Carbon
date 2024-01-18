package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
//公告
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement implements Serializable {
    private String id;
    private Timestamp time;
    private String title;
    private String content;
}
