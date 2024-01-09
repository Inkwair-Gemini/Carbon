package com.carbon.po.BulkAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

// 群组
public class Group implements Serializable {
    private String id;
    private String name;
    private String groupMaster;
    private Timestamp createTime;
    private Timestamp updateTime;
}