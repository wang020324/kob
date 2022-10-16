package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.pojo.User;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//实现注册
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
      Map<String,String>map=new HashMap<>();
      //如果用户名为空，告知用户用户名不能为空
        if(username==null){
            map.put("error_message","用户名不能为空");
            return map;
        }
        //如果密码或者确认密码项为空，告知用户密码不能为空
        if(password==null||confirmedPassword==null){
            map.put("error_message","密码不能为空");
            return map;
        }
        //一般将用户名的首尾空格删掉
        username=username.trim();
        //当用户名的长度为0时，告知用户用户名不能为空
        //当密码的长度为0，告知用户密码不能为空
        if(username.length()==0){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if(password.length()==0||confirmedPassword.length()==0){
            map.put("error_message","密码不能为空");
            return map;
        }
        //如果用户名/密码太长，告知用户用户名或密码不要太长
        if(username.length()>100){
            map.put("error_message","用户名不要太长超过100");
            return map;
        }
        if(password.length()>100||confirmedPassword.length()>100){
            map.put("error_message","密码不要太长超过100");
            return map;
        }

        //当输入密码与确认密码不一致，需告知用户两次密码输入不一致
      if(!password.equals(confirmedPassword)){
          map.put("error_message","两次输入的密码不一致");
          return map;
      }

      //输入的用户名在数据库中不能重复
        //定义查询条件
        QueryWrapper<User>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<User> users=userMapper.selectList(queryWrapper);//将查询结果存在list中
        //当查询结果不为空，则说明用户名已存在
        if(!users.isEmpty()){
            map.put("error_message","用户名已存在");
            return map;
        }
        //都成功就可以把用户存到数据库中，存的时候先加密

        //首先需要先把密码加密
        String encodedPassword = passwordEncoder.encode(password);
        String photo="https://cdn.acwing.com/media/user/profile/photo/109718_lg_84022218b5.webp";
        User user=new User(null,username,encodedPassword,photo);
        userMapper.insert(user);//加进数据库中
        //最终告知成功
        map.put("error_message","success");
        return map;
    }
}
