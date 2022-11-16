package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.RemoveService;
import com.kob.backend.tool.Tool;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        //取出用户
    /*    UsernamePasswordAuthenticationToken authenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();
*/
        Tool tool=new Tool();
        //取出bot的id用作判断
        int bot_id=Integer.parseInt(data.get("bot_id"));
        Bot bot=botMapper.selectById(bot_id);//从数据库中查找bot
        Map<String,String>map=new HashMap<>();
        //情况判断
        //没有要删除的bot
        if(bot==null){
            map.put("error_message","Bot不存在或者已经被删除");
            return map;
        }
        //用户不是作者不能删除

        if(!bot.getUserId().equals(tool.user.getId())){
            map.put("error_message","没有权限删除该Bot");
            return map;
        }
        //否则则可以直接删除
        botMapper.deleteById(bot_id);
        map.put("error_message","success");



        return map;
    }
}
