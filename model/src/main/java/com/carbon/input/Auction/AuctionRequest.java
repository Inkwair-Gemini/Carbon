package com.carbon.input.Auction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 单向竞价委托单
public class AuctionRequest implements Serializable {
    private String id;
    private Timestamp time; // 申请时间
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String accountType; // 账户类型
    private String quotaAccount; // 配额账户
    private Double price; // 委托价格
    private Double amount; // 委托数量
    private Double recentPrice; //最新价格
    private String operatorCode; // 操作员代码
    private String status; // 状态
}