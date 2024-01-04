package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 客户
public class Client implements Serializable {
    private String id; // 客户Id
    private String name;
    private String capitalAccountId; // 资金账户Id
    private String quotaAccountId; // 配额账户Id
}