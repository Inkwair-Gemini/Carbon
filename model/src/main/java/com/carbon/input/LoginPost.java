package com.carbon.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginPost implements Serializable {
    private String clientId;
    private String operatorId;
    private String password;
    private String captcha;
}
