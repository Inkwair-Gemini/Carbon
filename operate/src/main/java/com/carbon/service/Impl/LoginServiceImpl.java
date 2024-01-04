package com.carbon.service.Impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.carbon.input.ForgetPasswordPost;
import com.carbon.input.LoginPost;
import com.carbon.input.ModifyPasswordPost;
import com.carbon.mapper.ClientMapper;
import com.carbon.mapper.ClientOperatorMapper;
import com.carbon.po.Client;
import com.carbon.po.ClientOperator;
import com.carbon.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private ClientOperatorMapper ClientOperatorMapper;
    @Override
    public String login(LoginPost loginPost) {
        String captchaCode = null;
        //todo 获取验证码
        if(!loginPost.getCaptcha().equals(captchaCode)){
            return "验证码错误";
        }
        QueryWrapper query = new QueryWrapper<LoginPost>();
        query.eq("client_id",loginPost.getClientId());
        query.eq("operator_id",loginPost.getOperatorId());
        ClientOperator target = ClientOperatorMapper.selectOne(query);
        if(target.getPassword().equals(loginPost.getPassword())){
            return "登录成功";
        }
        else{
            return "密码错误";
        }
    }

    @Override
    public boolean modifyPassword(ModifyPasswordPost modifyPasswordPost) {
        if(!modifyPasswordPost.getNewPassword().equals(modifyPasswordPost.getReNewPassword())){
            return false;
        }
        QueryWrapper query = new QueryWrapper<LoginPost>();
        query.eq("client_id",modifyPasswordPost.getClientId());
        query.eq("operator_id",modifyPasswordPost.getOperatorId());
        ClientOperator target  = ClientOperatorMapper.selectOne(query);
        if(target.getPassword().equals(modifyPasswordPost.getPassword())){
            target.setPassword(modifyPasswordPost.getNewPassword());
            UpdateWrapper update = new UpdateWrapper<ClientOperator>();
            update.eq("client_id",modifyPasswordPost.getClientId());
            update.eq("operator_id",modifyPasswordPost.getOperatorId());
            update.set("password",modifyPasswordPost.getNewPassword());
            ClientOperatorMapper.update(null,update);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean forgetPassword(ForgetPasswordPost forgetPasswordPost) {
        if(!forgetPasswordPost.getNewPassword().equals(forgetPasswordPost.getRenewPassword())){
            return false;
        }
        String captchaCode = null;
        //todo 获取邮箱验证码
        if(!forgetPasswordPost.getEmailCaptcha().equals(captchaCode)){
            return false;
        }
        UpdateWrapper update = new UpdateWrapper<ClientOperator>();
        update.eq("client_id",forgetPasswordPost.getClientId());
        update.eq("operator_id",forgetPasswordPost.getOperatorId());
        update.set("password",forgetPasswordPost.getNewPassword());
        ClientOperatorMapper.update(null,update);
        return true;
    }
}
