package com.carbon.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 配额划转表单
public class QuotaTransferPost {
    private String id;
    private String quotaAccount; // 配额账号
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private String type; // 划转类型
    private Double actualAmount; // 划转数量
}