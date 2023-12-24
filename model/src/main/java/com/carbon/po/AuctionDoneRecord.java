package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 单向竞价成交记录
public class AuctionDoneRecord {
    private String id;
    private Date time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private Double finallyBalance; // 最终报价金额
    private String requestClient; // 卖方客户
    private String purchaserClient; // 买方客户
}