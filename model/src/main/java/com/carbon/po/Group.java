package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 群组
public class Group {
    private String id;
    private String name;
    private Date createTime;
    private Date updateTime;
    private List<String> members;
}