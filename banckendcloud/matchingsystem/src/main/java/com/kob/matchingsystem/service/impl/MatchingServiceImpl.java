package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    //定义全局变量，将matchingpool的类引进去
    public final static MatchingPool matchingPool =new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("add Player: "+userId+" "+rating);
        //将玩家加进匹配池
        matchingPool.addPlayer(userId,rating);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player: "+userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}