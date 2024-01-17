package com.carbon.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carbon.mapper.ClientMapper;
import com.carbon.mapper.ClientOperatorMapper;
import com.carbon.po.User.Client;
import com.carbon.po.User.ClientOperator;
import com.carbon.result.Result;
import com.carbon.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.carbon.security.redis.RedisCache;
import com.carbon.security.utils.JwtHelper;
import com.carbon.security.utils.MD5;
import java.util.HashMap;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private ClientOperatorMapper clientOperatorMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public Result login(String operatorCode,String clientId, String password) {
        Client client = clientMapper.selectOne(new LambdaQueryWrapper<Client>()
                .eq(Client::getId,clientId));
        if (client == null){
            return Result.fail("客户不存在");
        }

        ClientOperator clientOperator = clientOperatorMapper.selectOne(new LambdaQueryWrapper<ClientOperator>()
                .eq(ClientOperator::getId,operatorCode).eq(ClientOperator::getClientId,clientId));
        if (clientOperator == null){
            return Result.fail("该操作员不存在");
        }

        // 校验密码
        String newPassword = MD5.encrypt(password);
        if(!clientOperator.getPassword().equals(newPassword)){
            return Result.fail("密码错误");
        }

        // 使用id生成token
        String Auth = String.valueOf(clientOperator.getId());
        // 构造一个JWT 字符串
        String jwt = JwtHelper.createToken(Auth,clientId);

        //存入redis
        redisCache.setCacheObject("login:" + Auth,clientOperator);

        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return Result.ok(map);
    }

    @Override
    public void loginOut(String operatorCode) {
        redisCache.deleteObject("login:" + operatorCode);
    }
}
