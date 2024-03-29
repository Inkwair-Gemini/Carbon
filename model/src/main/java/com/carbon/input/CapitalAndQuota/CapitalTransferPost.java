package com.carbon.input.CapitalAndQuota;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor


// 资金划转表单
public class CapitalTransferPost implements Serializable {
    private String id;
    private String capitalAccount; // 资金账号
    @TableField("`type`")
    private String type; // 划转类型
    private Double actualAmount; // 划转金额
    private String password; // 密码
}