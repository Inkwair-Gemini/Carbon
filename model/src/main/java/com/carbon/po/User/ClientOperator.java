package com.carbon.po.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

// 客户操作员
public class ClientOperator implements Serializable {
    @JsonProperty(value = "operatorCode")
    private String id; // 操作员Id
    private String clientId; // 客户Id
    private String password; // 密码
}