package com.carbon.result;
import lombok.Getter;

//该枚举类含有两个成员，使用构造方法来给枚举值赋值
@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(400, "失败"),
    SERVICE_ERROR(405, "服务异常"),
    DATA_ERROR(403, "数据异常"),

    LOGIN_AUTH(410, "未登陆"),
    PERMISSION(409, "没有权限"),
    LOGIN_ERROR(408,"认证失败");

    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}