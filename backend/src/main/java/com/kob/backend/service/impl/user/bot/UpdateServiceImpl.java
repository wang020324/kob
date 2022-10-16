package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import com.kob.backend.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    public BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        /*
        UsernamePasswordAuthenticationToken authenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl)authenticationToken.getPrincipal();
        User user=loginUser.getUser();
        */
        Tool tool=new Tool();
        //取出bot的id以判断是否有权限更新
        int bot_id=Integer.parseInt(data.get("bot_id"));

        //取出会更新的元素的名称
        String title=data.get("title");
        String description=data.get("description");
        String content=data.get("content");

        //定义返回值
        Map<String,String>map=new HashMap<>();

        //修改限制
        if(title==null||title.length()==0){
            map.put("error_message","标题不能为空");
            return map;
        }
        if(title.length()>100){
            map.put("error_message","标题的长度不能大于100");
            return map;
        }
        if(description==null||description.length()==0){
            description="这个用户很懒，什么都没有留下~";
        }
        if(description.length()>300){
            map.put("error_message","Bot的描述长度不能大于300");
            return map;
        }
        if(content==null||content.length()==0){
            map.put("error_message","Bot的代码不能为空");
            return map;
        }
        if(content.length()>10000){
            map.put("error_message","Bot的代码的长度不能大于10000");
            return map;
        }

        //判断当前用户是否能够更新
        Bot bot=botMapper.selectById(bot_id);//依据bot的id来查找出bot
        if(bot==null){
            map.put("error_message","Bot已经被删除或者不存在");
            return map;

        }
        //当取出来的bot的用户id与当前用户的用户id不一致时，此时不能删除
        if(!bot.getUserId().equals(tool.user.getId())){
            map.put("error_message","没有权限修改该Bot");
            return map;

        }
        //建一个新的bot，放入新的值，然后将新的bot更新进去
        Bot new_bot=new Bot(
                bot.getId(),
                tool.user.getId(),
                title,
                description,
                content,
                bot.getRating(),
                bot.getCreatetime(),
                new Date()


        );
        botMapper.updateById(new_bot);
        map.put("error_message","success");
        return map;

    }
}
