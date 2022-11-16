package com.kob.backend.service.user.account;

import java.util.Map;

//接口部分（登录的接口部分)
public interface LoginService {
    //接口
    //所有的api最后都会返回一个JSON/MAP
    Map<String,String> getToken(String username, String password);
}
