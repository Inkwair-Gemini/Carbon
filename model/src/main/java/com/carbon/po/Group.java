package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

// 群组
public class Group {
    private String id;
    private String name;
    private Timestamp createTime;
    private Timestamp updateTime;
}