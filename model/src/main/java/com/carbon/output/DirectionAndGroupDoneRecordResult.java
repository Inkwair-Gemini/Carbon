package com.carbon.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DirectionAndGroupDoneRecordResult implements Serializable {
    private String id;
    private Timestamp time;
    private String groupId;
    private String subjectMatterCode;
    private String subjectMatterName;
    private String flowType;
    private Double firstPrice; // 单价
    private Double firstAmount; // 数量
    private Double firstBalance; // 初始报价金额
    private Double finallyPrice;
    private Double finallyAmount;
    private Double finallyBalance; // 最终报价金额
    private String listingClient; // 挂牌方
    private String delistingClient; // 摘牌方
}
