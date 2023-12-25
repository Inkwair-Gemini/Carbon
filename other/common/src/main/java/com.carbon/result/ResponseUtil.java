package com.carbon.result;

import java.io.IOException;


//当不是控制类却需要返回json时调用
public class ResponseUtil {
    public static void out(HttpServletResponse response, Result r) {
        //ObjectMapper是jackson库中用于处理json数据的
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);//返回类型为json数据
        try {
            //writeValue将Result对象序列化为json格式,controller里会自动做这一步，其他类不会，故现在需要写工具类
            mapper.writeValue(response.getWriter(), r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}