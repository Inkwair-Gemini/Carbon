package com.carbon.service;


import com.carbon.result.Result;

public interface LoginService {

    Result login(String operatorCode,String clientId , String password);

    void loginOut();
}