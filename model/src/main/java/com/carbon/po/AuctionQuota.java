package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 单向竞价商品
public class AuctionQuota {
    private String id;
    private Timestamp time; // 平台发布时间
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private Double price; // 委托价格
    private Double amount; // 委托数量
    private String totalBalance; // 总价
    private String clientId;
    // 剩余时间
    private String status; // 状态
}