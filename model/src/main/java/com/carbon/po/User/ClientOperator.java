package com.carbon.po.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 客户操作员
public class ClientOperator implements Serializable {
    private String id; // 操作员Id
    private String clientId; // 客户Id
    private String password; // 密码
}