package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.management.remote.rmi._RMIConnection_Stub;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread{
    //存下来所有的用户
    private static List<Player>players=new ArrayList<>();
    //定义一个锁
    private ReentrantLock lock = new ReentrantLock();

    //注入RestTemplate
    private static RestTemplate restTemplate;

    //定义url
    private  final static String startGameUrl="http://127.0.0.1:3000/pk/start/game/";

    //自动注入
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate =restTemplate;
    }

    //添加一名玩家
    public void addPlayer(Integer userId,Integer rating){
        lock.lock();
        try{
            players.add(new Player(userId,rating,0));

        }finally{
            lock.unlock();
        }

    }

    //删除一名玩家
    public void removePlayer(Integer userId){
        lock.lock();
        try{
            //新列表，将没有删的都存下来
            List<Player>newPlayers = new ArrayList<>();
            //枚举下所有的player
            for(Player player :players){
                if(!player.getUserId().equals(userId)){
                    newPlayers.add(player);
                }
            }
            players=newPlayers;
        }finally{
            lock.unlock();
        }
    }
    //辅助函数，用来当用户还没匹配的时候，等待时间+1
    private void increaseWaitingTime(){
        //循环，让还在匹配池里的等待玩家加1
        for(Player player:players){
            player.setWaitingTime(player.getWaitingTime()+1);
        }
    }

    //辅助函数:用来尝试匹配
    private void matchPlayers(){
        System.out.println("match players: "+players.toString());
        //定义boolean数组，以表示当前还剩下那些人
        boolean[] used =new boolean[players.size()];

        //匹配策略:越老的玩家越容易匹配
        //枚举下所有玩家
        for(int i=0;i<players.size();i++){
            if(used[i])continue;//该玩家如果已经被匹配，那么就跳过
            //从第一个确定的玩家i开始，向后找匹配对象，因为按顺序枚举，因此前面一定是匹配过的
            for(int j=i+1;j<players.size();j++){
                if(used[j])continue;//如果该玩家已经被匹配，那么就跳过
                //否则取出两名玩家
                Player a=players.get(i);
                Player b=players.get(j);
                if(checkMatched(a,b)){
                    used[i]=used[j]=true;//取出来匹配，将当前位置标记为true
                    sendResult(a,b);//发送信息
                    break;
                }

            }
        }
        //匹配完之后需要将已经用过的玩家删掉
        List<Player>newPlayers=new ArrayList<>();
        for(int i=0;i<players.size();i++){
            //如果这个玩家没有被用过，那就把他存下来，否则将其删掉
            if(!used[i]){
                newPlayers.add(players.get(i));
            }
        }
        players=newPlayers;//重新更新players
    }

    //辅助函数，用来判断两名玩家是否匹配
    private boolean checkMatched(Player a,Player b){
        //计算两名玩家的差值
       int ratingDelta=Math.abs(a.getRating()-b.getRating());
       //计算每名玩家的等待时间，并且取得最小值
        int waitingTime =Math.min(a.getWaitingTime(),b.getWaitingTime());
        //如果分差小于这两个的最小值那么就全部满足
        return ratingDelta<=waitingTime*10;
    }

    //辅助函数，当如果匹配的时候，用来返回函数结果
    private void sendResult(Player a,Player b){
     System.out.println("send Result"+ a +""+b);
     //发送请求前首先先构造参数
        MultiValueMap<String,String>data=new LinkedMultiValueMap<>();
        data.add("a_id",a.getUserId().toString());
        data.add("b_id",b.getUserId().toString());
        restTemplate.postForObject(startGameUrl,data,String.class);

    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();

                }finally{
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
