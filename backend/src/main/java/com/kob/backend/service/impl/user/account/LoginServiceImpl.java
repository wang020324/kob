package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.tool.Tool;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//一个实现(登录部分)

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public Map<String, String> getToken(String username, String password) {
        //封装用户名与密码
      UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);//将用户名与密码封装成一个类，类里面不会存明文而是加密之后的用户名与密码

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);// 当登录失败自动报异常

        //取出用户
        UserDetailsImpl loginUser =(UserDetailsImpl) authenticate.getPrincipal();
        User user=loginUser.getUser();

        //将UserId 封装成jwt_token
        String jwt= JwtUtil.createJWT(user.getId().toString());

        //返回结果
        Map<String,String>map=new HashMap<>();
        map.put("error_message","success");//如果失败了就会自动报异常就会自动处理,程序能够运行到这一般是成功的
        //将token放到结果中
        map.put("token",jwt);

        return map;
    }
}
