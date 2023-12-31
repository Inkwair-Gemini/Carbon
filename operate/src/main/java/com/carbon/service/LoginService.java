package com.carbon.service;

import com.carbon.input.ForgetPasswordPost;
import com.carbon.input.LoginPost;
import com.carbon.input.ModifyPasswordPost;

public interface LoginService {
    //登录
    String login(LoginPost loginPost);
    //修改密码
    boolean modifyPassword(ModifyPasswordPost modifyPasswordPost);
    //忘记密码
    boolean forgetPassword(ForgetPasswordPost forgetPasswordPost);
}
