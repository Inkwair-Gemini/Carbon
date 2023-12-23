package com.carbon.po;

import java.util.List;

// 客户
public class Client {
    private String id; // 客户Id
    private String name;
    private String capitalAccountId; // 资金账户Id
    private String quotaAccountId; // 配额账户Id
    private List<ClientOperator> operatorList; // 操作员列表
}