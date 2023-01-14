package com.kob.backend.service.impl.user.account.acwing;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.user.account.acwing.utils.HttpClientUtil;
import com.kob.backend.service.user.account.acwing.AcAppService;
import com.kob.backend.utils.JwtUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.*;

@Service
public class AcAppServiceImpl implements AcAppService {
    private final static String appId = "2753";
    private final static String appSecret = "22a42660d014482d8664cb1f57bee622";
    private final static String redirectUri = "https://app2753.acapp.acwing.com.cn/api/user/account/acwing/acapp/receive_code/";
    private final static String applyAccessTokenUrl = "https://www.acwing.com/third_party/api/oauth2/access_token/";
    private final static String getUserInfoUrl = "https://www.acwing.com/third_party/api/meta/identity/getinfo/";
    private final static Random random = new Random();
    //注入mapper
    @Autowired
    private UserMapper userMapper;

    //注入redis服务
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public JSONObject applyCode() {
        JSONObject resp = new JSONObject();
        resp.put("appid", appId);
        //对回调链接进行编码
        try {
            resp.put("redirect_uri", URLEncoder.encode(redirectUri, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            resp.put("result", "failed");
            return resp;
        }
        resp.put("scope", "userinfo");

        StringBuilder state = new StringBuilder();
        //定义随机字符串
        for (int i = 0; i < 10; i ++ )
            state.append((char) (random.nextInt(10) + '0'));
        resp.put("state", state.toString());
        resp.put("result", "success");
        //存进redis中，并且设置有效期
        redisTemplate.opsForValue().set(state.toString(), "true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10));  // 10分钟
        return resp;
    }

    @Override
    public JSONObject receiveCode(String code, String state) {
        JSONObject resp = new JSONObject();
        resp.put("result", "failed");
        if (code == null || state == null) return resp;//如果code不存在或者state不存在，直接返回失败
         //存在则判断redis中是否有state，存在则进行删除
        if (Boolean.FALSE.equals(redisTemplate.hasKey(state))) return resp;
        redisTemplate.delete(state);
        //定义url参数列表
        List<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("appid", appId));
        nameValuePairs.add(new BasicNameValuePair("secret", appSecret));
        nameValuePairs.add(new BasicNameValuePair("code", code));
        //获取申请结果
        String getString = HttpClientUtil.get(applyAccessTokenUrl, nameValuePairs);
        if (getString == null) return resp;
        JSONObject getResp = JSONObject.parseObject(getString);
        //取出access_token与openid，如果有一个值不存在，则表示失败
        String accessToken = getResp.getString("access_token");
        String openid = getResp.getString("openid");
        if (accessToken == null || openid == null) return resp;

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        List<User> users = userMapper.selectList(queryWrapper);
        //如果存在这个openid则说明有这个用户在Acwing服务器里
        if (!users.isEmpty()) {
            User user = users.get(0);//取出用户
            String jwt = JwtUtil.createJWT(user.getId().toString());
            resp.put("result", "success");
            resp.put("jwt_token", jwt);
            return resp;
        }
        //不存在则向用户发送请求获取用户信息
        nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairs.add(new BasicNameValuePair("openid", openid));
        getString = HttpClientUtil.get(getUserInfoUrl, nameValuePairs);
        if (getString == null) return resp;
        getResp = JSONObject.parseObject(getString);
        String username = getResp.getString("username");
        String photo = getResp.getString("photo");

        //添加随机数，以防止重名情况出现
        //如果用户名或者照片为空，那么直接返回失败信息
        if (username == null || photo == null) return resp;
        for (int i = 0; i < 100; i ++ ) {
            QueryWrapper<User> usernameQueryWrapper = new QueryWrapper<>();
            usernameQueryWrapper.eq("username", username);
            if (userMapper.selectList(usernameQueryWrapper).isEmpty()) break;
            username += (char)(random.nextInt(10) + '0');
            if (i == 99) return resp;
        }

        User user = new User(
                null,
                username,
                null,
                photo,
                1500,
                openid
        );

        userMapper.insert(user);
        String jwt = JwtUtil.createJWT(user.getId().toString());
        resp.put("result", "success");
        resp.put("jwt_token", jwt);
        return resp;
    }
}

