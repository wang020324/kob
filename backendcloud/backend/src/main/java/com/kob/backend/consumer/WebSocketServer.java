package com.kob.backend.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.awt.image.ImageWatched;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    //全局静态对于所有websocket可见的变量，用来存储所有链接
    final public static ConcurrentHashMap<Integer,WebSocketServer>users=new ConcurrentHashMap<>();
    //定义匹配池
    //final private static CopyOnWriteArraySet<User>matchpool=new CopyOnWriteArraySet<>();

    //定义成员变量
    private User user;


    private Session session=null;

    //定义一个独一份的变量
    public static UserMapper userMapper;

    //定义类，存储Game
    public Game game= null;

    //定义数据库Mapper
    private static BotMapper botMapper;

    //url
    private final static String addPlayerUrl ="http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";

    //定义变量，将Game中定义的有关数据库的函数加到类里面
    public static RecordMapper recordMapper;

    //定义变量，注入工具
    public static RestTemplate restTemplate;//发送请求

    @Autowired
    //赋值到set函数里
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper=userMapper;
    }
    //添加set函数给数据库
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper=recordMapper;
    }
    //添加注解注入工具类
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate=restTemplate;
    }
    //添加注入BotMapper
    @Autowired
    public void setBotMapper(BotMapper botMapper){
        WebSocketServer.botMapper=botMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session=session;
        System.out.println("connected!");
        //Integer userId=Integer.parseInt(token);//取出用户id
        Integer userId= JwtAuthentication.getUserId(token);
        //通过userId知道用户是谁
        this.user=userMapper.selectById(userId);
        //获取用户之后将用户存下来
        if(this.user!=null) {
            users.put(userId, this);

        }else{
            this.session.close();
        }
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected!");
        //断开连接则删除user
        if(this.user!=null){
            users.remove(this.user.getId());
           // matchpool.remove(this.user);//断开连接，从匹配池删除
        }
    }

    public static void startGame(Integer aId,Integer aBotId,Integer bId,Integer bBotId){
        User a=userMapper.selectById(aId),b=userMapper.selectById(bId);
        //取出两名Bot
        Bot botA = botMapper.selectById(aBotId);
        Bot botB = botMapper.selectById(bBotId);

        //初始化角色位置需要传入id
        Game game=new Game(
                13,
                14,
                20,
                a.getId(),
                botA,
                b.getId(),
                botB
        );//取出地图
        game.createMap();//在后端创建地图
        if(users.get(a.getId())!=null)
           users.get(a.getId()).game=game;
        if(users.get(b.getId())!=null)
           users.get(b.getId()).game=game;

        game.start();//定义以实现多回合

        //生成A玩家
        JSONObject respGame = new JSONObject();
        respGame.put("a_id",game.getPlayerA().getId());
        respGame.put("a_sx",game.getPlayerA().getSx());
        respGame.put("a_sy",game.getPlayerA().getSy());


        //生成B玩家的位置以及地图
        respGame.put("b_id",game.getPlayerB().getId());
        respGame.put("b_sx",game.getPlayerB().getSx());
        respGame.put("b_sy",game.getPlayerB().getSy());
        respGame.put("map",game.getG());
        //首先对玩家A进行配对

        JSONObject respA=new JSONObject();
        respA.put("event","start-matching");//传操作类型
        //传过去对手的信息
        respA.put("opponent_username",b.getUsername());
        respA.put("opponent_photo",b.getPhoto());
        respA.put("game",respGame);

        //获取users链接，将respA的信息发给前端
        if(users.get(a.getId())!=null)
          users.get(a.getId()).sendMessage(respA.toJSONString());

        //同理获取下B的
        JSONObject respB=new JSONObject();
        respB.put("event","start-matching");//传操作类型
        //传过去对手的信息
        respB.put("opponent_username",a.getUsername());
        respB.put("opponent_photo",a.getPhoto());
        respB.put("game",respGame);
        //获取users链接，将respA的信息发给前端
        if(users.get(b.getId())!=null)
         users.get(b.getId()).sendMessage(respB.toJSONString());
    }
    private void startMatching(Integer botId){
        System.out.println("start matching");
       // matchpool.add(this.user);
        //傻瓜式匹配操作
       /* while(matchpool.size()>=2){
            Iterator<User>it=matchpool.iterator();//创建迭代系
            User a=it.next(),b=it.next();//随机取出两个玩家
            matchpool.remove(a);
            matchpool.remove(b);//匹配完成后将两个取出匹配池


        }

        */
        //首先定义请求的类型
        MultiValueMap<String,String>data=new LinkedMultiValueMap<>();
        //添加参数以传过去
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        data.add("bot_id",botId.toString());
        //发送请求
        restTemplate.postForObject(addPlayerUrl,data,String.class);


    }
    private void stopMatching(){
        System.out.println("stop matching");
        //atchpool.remove(this.user);//取消匹配则从匹配池中移除
        MultiValueMap<String,String>data=new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
    }
    //辅助函数,用来给setNextStep添加方向
    private void move(int direction){
        //b也要单独判断。以防止出现bug
        if(game.getPlayerA().getId().equals(user.getId())){
            if(game.getPlayerA().getBotId().equals(-1))//如果是人工操作才去放行人工操作
               game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
             if(game.getPlayerB().getBotId().equals(-1))
               game.setNextStepB(direction);
        }
    }

    //信息路由
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data=JSONObject.parseObject(message);//将message的信息从JSON中解析出来
        String event=data.getString("event");
        //这种的equal的写法会少很多的异常
        if("start-matching".equals(event)){
            startMatching(data.getInteger("bot_id"));

        }else if("stop-matching".equals(event)){
            stopMatching();
        }else if("move".equals(event)){
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    //定义辅助函数来让后端向前端发送信息
    public void sendMessage(String message){
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}