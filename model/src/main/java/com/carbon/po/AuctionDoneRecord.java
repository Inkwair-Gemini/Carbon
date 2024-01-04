package com.carbon.po;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 单向竞价成交记录
public class AuctionDoneRecord implements Serializable{
    private String id;
    private Timestamp time;
    private String subjectMatterCode;
    private String subjectMatterName;
    private Double finallyBalance; // 最终报价金额
    private String requestClient; // 卖方客户
    private String purchaserClient; // 买方客户
}