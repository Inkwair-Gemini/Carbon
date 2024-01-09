package service;


import com.carbon.result.Result;

public interface LoginService {
    Result login(String username, String password);

    void logout();
}