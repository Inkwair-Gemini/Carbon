package filter;

import com.alibaba.fastjson.JSONObject;
import redis.RedisCache;
import utils.JwtHelper;
import utils.ResponseUtil;
import com.carbon.result.Result;
import com.carbon.result.ResultCodeEnum;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * 每次请求的 Security 过滤类。执行jwt有效性检查
 */
@WebFilter("/*")
public class JwtAuthenticationTokenFilter implements Filter {

    private WebApplicationContext applicationContext;

    private String[] excludeUrls = new String[]{"/user/login"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        RedisCache redisCache = applicationContext.getBean(RedisCache.class);

        // 获取请求路径
        String requestURI = request.getRequestURI();

        // 如果请求路径为不需要过滤的路径，则直接放行
        if (Arrays.asList(excludeUrls).contains(requestURI)) {
            filterChain.doFilter(request, response);
            System.out.println("登陆请求，直接放行");
            return;
        }

        System.out.println("第1步：进入jwt过滤器");

        // 获取请求头中的 Authorization 字段
        String authorizationHeader = request.getHeader("Authorization");
        String token = "";

        // 判断 Authorization 字段是否存在且以 Bearer 开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 提取出 token
            System.out.println("第1.1步：获取token");
            token = authorizationHeader.substring(7); // 去除 Bearer 后面的空格，提取出 token
        }else {
            System.out.println("无token转登陆...");
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_AUTH));
            return;
        }
        System.out.println("有token获取认证信息...");

        //解析token
        String operatorCode = "";
        try {
            System.out.println("第1.2步：解析token");
            // 获取jwt中的用户名（或id）
            operatorCode = JwtHelper.getOperatorCode(token);
        } catch (Exception e) {
            ResponseUtil.out(response, Result.build(null,ResultCodeEnum.LOGIN_ERROR));
            return;
        }

        //从redis中获取用户信息
        String redisKey = "login:" + operatorCode;
        JSONObject loginUser = redisCache.getCacheObject(redisKey);
        System.out.println("第1.3步：判断redis中是否有数据");
        if(Objects.isNull(loginUser)){
            ResponseUtil.out(response, Result.build(null,ResultCodeEnum.LOGIN_AUTH));
            return;
        }

        System.out.println("第1.4步：放行");
        //放行
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
