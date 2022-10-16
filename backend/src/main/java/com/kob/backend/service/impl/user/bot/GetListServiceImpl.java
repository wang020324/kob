package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.GetListService;
import com.kob.backend.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetListServiceImpl implements GetListService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public List<Bot> getList() {
        //取出当前用户
        Tool tool=new Tool();

        //定义封装查询条件，并将查询结果返回
        QueryWrapper <Bot>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",tool.user.getId());

        return botMapper.selectList(queryWrapper);

    }
}
