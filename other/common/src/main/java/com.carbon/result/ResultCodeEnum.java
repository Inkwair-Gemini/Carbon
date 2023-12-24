package com.carbon.result;
import lombok.Getter;

//该枚举类含有两个成员，使用构造方法来给枚举值赋值
@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),

    LOGIN_AUTH(209, "未登陆"),
    PERMISSION(210, "没有权限"),
    LOGIN_ERROR(208,"认证失败");

    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}