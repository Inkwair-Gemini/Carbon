package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor

// 公告
public class Announcement {
    private String id;
    private Date time;
    private String content;
    private String type;
}