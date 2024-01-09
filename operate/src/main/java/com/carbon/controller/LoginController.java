package com.carbon.controller;
import com.carbon.po.User.ClientOperator;
import com.carbon.result.Result;
import com.carbon.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    LoginService loginService;

    // 操作员登录
    @PostMapping("/login")
    public Result login(@RequestBody ClientOperator clientOperator){
        return loginService.login(clientOperator.getId(), clientOperator.getClientId(),clientOperator.getPassword());
    }
    //登出
    @GetMapping("/loginOut")
    public Result loginOut(){
        loginService.loginOut();
        return Result.ok();
    }
}
