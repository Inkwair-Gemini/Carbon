package com.carbon.securityTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import utils.MD5;

@SpringBootTest
public class sTest {
    @Test
    public void test(){
        System.out.println(MD5.encrypt("123456"));
    }
}
