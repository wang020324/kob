package com.kob.backend.service.user.account.acwing;

import com.alibaba.fastjson.JSONObject;

public interface AcAppService {
    JSONObject applyCode();//申请回调url
    JSONObject receiveCode(String code,String state);//回调url

}
