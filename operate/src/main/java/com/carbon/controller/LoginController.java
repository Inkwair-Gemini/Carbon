package com.carbon.controller;

import com.carbon.input.LoginPost;
import com.carbon.result.Result;
import com.carbon.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    LoginService LoginService;

    // 操作员登录
    @PostMapping("/login")
    public Result Login(@RequestBody LoginPost loginPost){
        try{
            String reply=LoginService.login(loginPost);
            if (reply.equals("登录成功"))
                return Result.ok(reply);
            else
                return Result.fail(reply);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }
}
