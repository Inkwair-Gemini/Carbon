package com.carbon.service.Impl;
import com.carbon.input.LoginPost;
import com.carbon.service.LoginService;

public class LoginServiceImpl implements LoginService {

    @Override
    public String login(String clientId, String operatorId, String password, String captcha) {
        String captchaCode = null;
        //todo 获取验证码
        if(!captcha.equals(captchaCode)){
            return "验证码错误";
        }
        LoginPost target = LoginDao.getLoginPost(clientId,operatorId);
        if(target.getPassword().equals(password)){
            return "登录成功";
        }
        else{
            return "密码错误";
        }
    }

    @Override
    public boolean modifyPassword(String clientId, String operatorId, String password,
                                  String newPassword, String renewPassword) {
        if(!newPassword.equals(renewPassword)){
            return false;
        }
        LoginPost target = LoginDao.getLoginPost(clientId,operatorId);
        if(target.getPassword().equals(password)){
            target.setPassword(newPassword);
            LoginDao.updateLoginPost(target);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean forgetPassword(String clientId, String operatorId, String emailCaptcha, String newPassword) {
        String captchaCode = null;
        //todo 获取邮箱验证码
        if(!emailCaptcha.equals(captchaCode)){
            return false;
        }
        LoginPost target = LoginDao.getLoginPost(clientId,operatorId);
        target.setPassword(newPassword);
        LoginDao.updateLoginPost(target);
        return true;
    }
}
