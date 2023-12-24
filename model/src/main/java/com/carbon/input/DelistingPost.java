package com.carbon.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 摘牌表单
public class DelistingPost {
    private String id;
    private String ListingPostId; // 挂牌表单ID
    private String accountType; // 账户类型
    private String quotaAccount; // 配额账户
    private Double delistAmount; // 摘牌数量
    private String operatorCode; // 操作员代码
    private String status; // 状态
}