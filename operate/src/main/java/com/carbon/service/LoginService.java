package com.carbon.service;

public interface LoginService {
    //登录
    String login(String clientId,String operatorId, String password,String captcha);
    //修改密码
    boolean modifyPassword(String clientId,String operatorId, String password,String newPassword,String renewPassword);
    //忘记密码
    boolean forgetPassword(String clientId,String operatorId,String emailCaptcha,String newPassword);
}
