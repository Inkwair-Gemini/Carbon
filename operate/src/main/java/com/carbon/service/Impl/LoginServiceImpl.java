package com.carbon.service.Impl;
import com.carbon.input.ForgetPasswordPost;
import com.carbon.input.LoginPost;
import com.carbon.input.ModifyPasswordPost;
import com.carbon.service.LoginService;

public class LoginServiceImpl implements LoginService {

    @Override
    public String login(LoginPost loginPost) {
        String captchaCode = null;
        //todo 获取验证码
        if(!loginPost.getCaptcha.equals(captchaCode)){
            return "验证码错误";
        }
        LoginPost target = LoginDao.getLoginPost(loginPost.getClientId(),loginPost.getOperatorId());
        if(target.getPassword().equals(loginPost.getPassword())){
            return "登录成功";
        }
        else{
            return "密码错误";
        }
    }

    @Override
    public boolean modifyPassword(ModifyPasswordPost modifyPasswordPost) {
        if(!modifyPasswordPost.getNewPassword.equals(modifyPasswordPost.getRenewPassword)){
            return false;
        }
        LoginPost target = LoginDao.getLoginPost(modifyPasswordPost.getClientId,modifyPasswordPost.getOperatorId);
        if(target.getPassword().equals(modifyPasswordPost.getPassword)){
            target.setPassword(modifyPasswordPost.getNewPassword);
            LoginDao.updateLoginPost(target);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean forgetPassword(ForgetPasswordPost forgetPasswordPost) {
        if(!forgetPasswordPost.getNewPassword.equals(forgetPasswordPost.getRenewPassword)){
            return false;
        }
        String captchaCode = null;
        //todo 获取邮箱验证码
        if(!forgetPasswordPost.getEmailCaptcha.equals(captchaCode)){
            return false;
        }
        LoginPost target = LoginDao.getLoginPost(forgetPasswordPost.getClientId,forgetPasswordPost.getOperatorId);
        target.setPassword(forgetPasswordPost.getNewPassword);
        LoginDao.updateLoginPost(target);
        return true;
    }
}
