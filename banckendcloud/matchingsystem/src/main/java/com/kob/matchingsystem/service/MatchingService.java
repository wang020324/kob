package com.kob.matchingsystem.service;

public interface MatchingService {
    //定义接口函数
    String addPlayer(Integer userId,Integer rating);
    String removePlayer (Integer userId);
}

