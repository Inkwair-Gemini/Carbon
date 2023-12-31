package com.carbon.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 客户登记配额
public class ClientRegisterQuota implements Serializable {
    private String clientId; // 客户Id
    private String subjectMatterCode; // 标的物代码
    private String subjectMatterName; // 标的物名称
    private Double amount; // 总数量
}