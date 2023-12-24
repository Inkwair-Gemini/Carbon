package com.carbon.result;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code; // 状态码
    private String message; // 返回信息
    private T data; // 统一返回的结果数据

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        // 封装数据
        if (body!=null){
            result.setData(body);
        }
        // 状态码
        result.setCode(resultCodeEnum.getCode());
        //返回信息
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    // 构造私有化 外部不能new
    private Result(){}

    // 成功 空结果
    public static <T> Result<T> ok(){
        return build(null,ResultCodeEnum.SUCCESS);
    }

    // 成功 有数据
    public static <T> Result<T> ok(T data){
        return build(data,ResultCodeEnum.SUCCESS);
    }

    // 失败 无结果
    public static <T> Result<T> fail(){
        return build(null,ResultCodeEnum.FAIL);
    }

    //失败  有结果
    public static <T> Result<T> fail(T data){
        return build(data,ResultCodeEnum.FAIL);
    }

    public Result <T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result <T> code(Integer code){
        this.setCode(code);
        return this;
    }
}