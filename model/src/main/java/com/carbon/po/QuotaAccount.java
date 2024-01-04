package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 配额账户
public class QuotaAccount implements Serializable {
    private String id;
    private String clientId;
    private String password;
    private String accountType; // 账户类型
}