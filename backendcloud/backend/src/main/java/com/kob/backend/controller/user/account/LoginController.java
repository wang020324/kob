package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//调用

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    //登录
    @PostMapping("/api/user/account/token/")

    //取出post的参数放到map里面
    public Map<String,String> getToken(@RequestParam Map<String,String>map){
        //取出用户名与密码
        String username =map.get("username");
        String password =map.get("password");
        System.out.println(username+' '+password);
        return loginService.getToken(username,password);//调用接口
    }


}
