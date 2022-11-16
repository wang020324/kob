package com.kob.matchingsystem;

import com.kob.matchingsystem.controller.MatchingController;
import com.kob.matchingsystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchingSystemApplciation {
    public static void main(String[] args) {
        //启动匹配池线程
        MatchingServiceImpl.matchingPool.start();

        SpringApplication.run(MatchingSystemApplciation.class,args);
    }
}