package com.carbon.po.BulkAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupClient implements Serializable {
    private String groupId; //群组
    private String clientId; //成员
}
