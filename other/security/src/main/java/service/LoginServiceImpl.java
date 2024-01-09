package service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import utils.JwtHelper;
import utils.MD5;
import com.carbon.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.RedisCache;
import java.util.HashMap;

import static com.carbon.utils.LoginUserInfoHelper.*;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private MyUserMapper userMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public Result login(String username, String password) {
        //根据用户名从数据库中查询用户
        MyUser user = userMapper.selectOne(new LambdaQueryWrapper<MyUser>()
                .eq(username != null, MyUser::getUsername, username));
        if (user == null) {
            return Result.fail("用户不存在");
        }
        // 校验密码
        String newPassword = MD5.encrypt(password);
        if(!user.getPassword().equals(newPassword)){
            return Result.fail("密码错误");
        }

        // 使用id生成token
        String userId = String.valueOf(user.getId());
        // 构造一个JWT 字符串
        String jwt = JwtHelper.createToken(userId,username);

        //存入redis
        redisCache.setCacheObject("login:" + userId,user);

        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return Result.ok(map);
    }

    @Override
    public void logout() {
        String userid = getUserId();
        redisCache.deleteObject("login:" + 1);
    }
}
