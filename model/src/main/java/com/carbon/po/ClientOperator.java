package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 客户操作员
public class ClientOperator {
    private String id; // 操作员Id
    private String clientId; // 客户Id
    private String password; // 密码
    private String email; // 邮件地址
}