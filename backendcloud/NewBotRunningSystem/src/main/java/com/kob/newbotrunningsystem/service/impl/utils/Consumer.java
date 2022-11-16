package com.kob.newbotrunningsystem.service.impl.utils;

import com.kob.newbotrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
@Component

public class Consumer extends Thread {
    private Bot bot;
    private static RestTemplate restTemplate;

    //定义url
    private final static String receiveBotMoveUrl ="http://127.0.0.1:3000/pk/receive/bot/move/";

    //注入RestTemplate
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate=restTemplate;
    }

    //定义函数以执行操作
    public void startTimeout(long timeout,Bot bot){
        //在函数里，首先先把bot存下来
        this.bot=bot;

        //启动当前线程
        this.start();

        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.interrupt();//如果超过最长的等待时间5s，则中断掉
        }
    }

    //定义一个辅助函数以实现代码Bot添加随机字符串的逻辑
    private String addUId(String code,String uid){
        //首先先从代码里找出字符串
        int k =code.indexOf(" implements com.kob.newbotrunningsystem.utils.BotInterface");
        //然后把第k位前面的代码加上+uid返回然后再返回后面的
        return code.substring(0,k)+ uid +code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();//添加一个随机的字符串，以保证多次编译
        String uid=uuid.toString().substring(0,8);//返回前8位的字符串0-8

        BotInterface botInterface = Reflect.compile(
                "com.kob.newbotrunningsystem.utils.Bot" + uid,
                addUId(bot.getBotCode(),uid)
        ).create().get();

        Integer direction =botInterface.nextMove(bot.getInput());
        System.out.println("move-direction:"+bot.getUserId()+" "+direction);
        //返回信息给Service
        MultiValueMap<String,String>data = new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);

    }
}
