package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 挂牌交易成交记录
public class ListingDoneRecord {
    private String id;
    private Date time;
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String flowType; // 买入/卖出
    private Double dealPrice; // 成交价格
    private Double dealAmount; // 成交数量
    private Double dealBalance; // 成交金额
    private String listingClient; // 挂牌方
    private String delistingClient; // 摘牌方
}