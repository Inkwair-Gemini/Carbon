package com.carbon.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelistingPost implements Serializable {
    private String id;
    private Timestamp time;
    private String ListingId;//挂牌号
    private String QuotaAccount;//库存账号
    private String accountType; // 账户类型
    private String flowType; // 买入/卖出
    private String listingType; //挂牌方式
    private Double amount; // 买入/卖出数量
    private String operatorCode; // 操作员代码
}
