package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl  implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer aBotId,Integer bId,Integer bBotId) {
        //输出调试信息
        System.out.println("start game:" + aId + " " + bId);
        //调用实现函数
        WebSocketServer.startGame(aId,aBotId,bId,bBotId);
        return "start game success";
    }
}
