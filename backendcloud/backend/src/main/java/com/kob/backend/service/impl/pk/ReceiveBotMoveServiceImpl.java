package com.kob.backend.service.impl.pk;


import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service

public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {

        //方便调试输出调试信息
        System.out.println("receive bot move:"+userId+" "+direction+" "+" " );
        //首先要判断下user对应的game是否存在,如果匹配池中有当前用户，则将其从Game中取出来
        if(WebSocketServer.users.get(userId)!=null){
            Game game =WebSocketServer.users.get(userId).game;
            //如果game不为空则可以执行下面的操作
            if(game!=null){
                if(game.getPlayerA().getId().equals(userId)){

                        game.setNextStepA(direction);
                }else if(game.getPlayerB().getId().equals(userId)){

                        game.setNextStepB(direction);
                }
            }
        }

        return "receive bot move success";
    }
}
