package com.kob.backend.service.user.account;

import java.util.Map;

//定义注册接口
public interface RegisterService {
    Map<String,String> register(String username,String password,String confirmedPassword);
}
