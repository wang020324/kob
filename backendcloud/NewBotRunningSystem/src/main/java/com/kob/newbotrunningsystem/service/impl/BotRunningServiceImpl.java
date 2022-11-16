package com.kob.newbotrunningsystem.service.impl;

import com.kob.newbotrunningsystem.service.BotRunningService;
import com.kob.newbotrunningsystem.service.impl.utils.Bot;
import com.kob.newbotrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    //单开静态变量来存储线程
    public final static BotPool botPool =new BotPool();
    @Override
    public String addBot(Integer userId, String  botCode, String input) {
        System.out.println("add bot" +userId+ " "+botCode +" "+input);
        botPool.addBot(userId,botCode,input);//从队列里插入一个bot

        return "add bot success";
    }
}
