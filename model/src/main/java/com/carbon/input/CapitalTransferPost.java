package com.carbon.intput;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 资金划转表单
public class CapitalTransferPost {
    private String id;
    private String capitalAccount; // 资金账号
    private String type; // 划转类型
    private Double actualAmount; // 划转金额
    private String password; // 密码
}