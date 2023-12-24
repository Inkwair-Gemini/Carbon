package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 客户
public class Client {
    private String id; // 客户Id
    private String name;
    private String capitalAccountId; // 资金账户Id
    private String quotaAccountId; // 配额账户Id
    private List<ClientOperator> operatorList; // 操作员列表
}