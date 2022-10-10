package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//接口实现
@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public Map<String, String> getInfo() {
        //将信息从上下文当中提取出来
        UsernamePasswordAuthenticationToken authentication=(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        //寻找User
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user =loginUser.getUser();
        Map<String,String>map=new HashMap<>();
        map.put("error_message","success");//同样也是失败了就会正常处理.抛出异常
        map.put("id",user.getId().toString());
        map.put("username",user.getUsername());
        map.put("photo",user.getPhoto());
        return map;
    }
}
