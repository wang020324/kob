package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    //先定义一些私有变量
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;//墙的数量
    final private int[][] g;//整个地图
    final private static int[] dx={-1,0,1,0},dy={0,1,0,-1};//放障碍物的偏移量

    //定义player类，用来存放对手的位置信息
    private final Player playerA,playerB;

    //定义成员变量获取用户的下一步操作
    private Integer nextStepA =null;
    private Integer nextStepB = null;

    //加锁，防止因为线程的读写出现冲突
    private ReentrantLock lock =new ReentrantLock();

   public String status = "playing";//表示游戏现在的状态,playing =>finished

    //存储变量，判断下哪一条蛇没有输入
    private String loser="";
    //all:平局 A:A输 B:B输

    //构造函数
    public Game(Integer rows,Integer cols,Integer inner_walls_count,Integer idA,Integer idB){

        this.rows=rows;
        this.cols=cols;
        this.inner_walls_count=inner_walls_count;
        this.g =new int[rows][cols];
        //初始化a和b玩家的id
        playerA=new Player(idA,rows-2,1,new ArrayList<>());
        playerB =new Player(idB,1,cols-2,new ArrayList<>());
    }

    //定义Get函数，保证能够访问的到
    public Player getPlayerA(){
        return playerA;
    }
    public Player getPlayerB(){
        return playerB;
    }
    //生成地图
    public int[][] getG(){
        return g;
    }

    //定义set函数用来设置两个变量的值
    public void setNextStepA(Integer nextStepA){
        lock.lock();
        try{
            this.nextStepA=nextStepA;
        }finally {
            lock.unlock();//把进程解锁，这样报异常也会解锁
        }

    }
    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try{
            this.nextStepB=nextStepB;
        }finally{
            lock.unlock();//把进程解锁，这样报异常也会解锁
        }

    }

    //判断连通性
    private boolean check_connectivity(int sx,int sy,int tx,int ty){
        if(sx==tx&&sy==ty)return true;
        g[sx][sy]=1;

        for(int i=0;i<4;i++){
            int x=sx+dx[i],y=sy+dy[i];
            //在整个地图内部
            if(x>=0&&x<this.rows&&y>=0&&y<this.cols&&g[x][y]==0){
                if(check_connectivity(x,y,tx,ty)){
                    g[sx][sy]=0;
                    return true;
                }
            }
        }
        g[sx][sy]=0;
        return false;
    }


    //画地图
    private boolean draw(){
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.cols;j++){
                g[i][j]=0;
            }
        }
        //给四周一圈添加障碍物
        for(int r=0;r<this.rows;r++){
            g[r][0]=g[r][this.cols-1]=1;
        }
        for(int c=0;c<this.cols;c++){
            g[0][c]=g[this.rows-1][c]=1;
        }
        Random random=new Random();
        //随机生成障碍物
        for(int i=0;i<this.inner_walls_count/2;i++){
            //循环1000次以随机生成
            for(int j=0;j<1000;j++){
                int r=random.nextInt(this.rows);
                int c=random.nextInt(this.cols);

                //如果当前位置已经有了障碍物或者说周边墙，跳过
                if(g[r][c]==1||g[this.rows-1-r][this.cols-1-c]==1)
                    continue;
                //此外空出里面地图的左下角和右上角，来放蛇
                if(r==this.rows-2&&c==1|| r==1 &&c==this.cols-2){
                    continue;
                }
                g[r][c]=g[this.rows-1-r][this.cols-1-c]=1;//否则放上障碍物
                break;//放完一半跳出
            }

        }
        return check_connectivity(this.rows-2,1,1,this.cols-2);
    }
    //画是随机生成的，因此还需要在最外层包一层函数
    //随机个1000次，画带有随机障碍物的不同的地图。
    public void createMap(){
        for(int i=0;i<1000;i++){
            if(draw())
                break;
        }
    }
    public boolean nextStep(){
        //取最小的睡眠时间，以获得完整的更新，否则防止被多次输入覆盖最终结果
        try{
            Thread.sleep(200);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

       for(int i=0;i<50;i++){
           try{
               Thread.sleep(100);
               lock.lock();
               try{
                   if(nextStepA!=null&&nextStepB!=null){
                       //添加更新下一步操作
                       playerA.getSteps().add(nextStepA);
                       playerB.getSteps().add(nextStepB);
                       return true;
                   }
               }finally{
                   lock.unlock();
               }
           } catch (InterruptedException e) {
            e.printStackTrace();
           }
       }
       return false;
    }
    //创建函数，用来将取出来的信息转化成String
    private String getMapString(){
        StringBuilder res=new StringBuilder();
        for(int i=0;i<rows;i++){
           for(int j=0;j<cols;j++){
               res.append(g[i][j]);
           }
        }
        return res.toString();
    }
    //创建对战输赢信息的数据库
    private void sendToDatabase(){
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }


    //装载向两名玩家广播结果信息信息
    private void sendResult(){
        JSONObject resp=new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        sendAllMessage(resp.toJSONString());
        sendToDatabase();

    }

    //首先先求一下长度并且把a的最后一位取出来

    //judge的辅助函数，用来判断游戏的合法性(首先以A为首位度)
    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB) {
        //首先先求一下长度
        int n = cellsA.size();
        //然后取出A的蛇的最后一部分
        Cell cell = cellsA.get(n - 1);
        //判断蛇的最后一位是不是墙,如果是墙，则游戏结束
        if (g[cell.x][cell.y] == 1) return false;
        //否则判断下蛇的最后一位与A是否有重合(也就是蛇自己撞自己以及蛇倒着走自杀)以及A的最后一位是否和B的不是最后一位相撞(也就是A蛇头撞B
        //蛇身,撞上则返回false，否则则返回true
        for (int i = 0; i < n - 1; i++) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y)
                return false;
        }
        for (int i = 0; i < n - 1; i++) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y)
                return false;
        }
      return true;

    }

    //辅助函数，用来判断两名玩家下一步操作是否合法
    private void judge(){
       //从Cell的返回的函数里取出两条蛇
        List<Cell>cellsA=playerA.getCells();
        List<Cell>cellsB=playerB.getCells();
        //在judge中进行判断
        boolean validA =check_valid(cellsA,cellsB);//以A为主操作，以b为辅助操作
        boolean validB =check_valid(cellsB,cellsA);//以B为主操作以A为辅助操作
        //只要是A、B双方有一方不合法，则游戏结束，并且可以判断下、
        if(!validA||!validB){
            status="finished";//一定要首先先判断整个游戏平局
            if(!validA||!validB){
                loser="all";
            }else if(!validA){
                loser="A";
            }else{
                loser="B";
            }

        }
    }
    //辅助函数，以实现发送信息的功能
    private void sendAllMessage(String message){
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);//将a的信息传递给链接
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);//将b的信息传递给b
    }
    //辅助函数，用来向双方装载广播移动信息
    private void sendMove(){
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepB=nextStepA=null;//移动信息传递完成后需要将信息清空
        }finally{
            lock.unlock();
        }
    }

    @Override
    public void run() {
        //最多600步，循环1000次来进行每一步判断
        for(int i=0;i<1000;i++){
            if(nextStep()){
                 judge();//辅助函数judge，判断两名玩家的下一步操作是否合法
                 //如果游戏还继续那么就广播整个下一步操作,返回结果输出
                if(status.equals("playing")){
                    sendMove();
                }else{
                    sendResult();
                    break;
                }
            }else{
                status="finished";
                lock.lock();
                try{
                    if(nextStepA==null&&nextStepB==null){
                        loser="all";
                    }else if(nextStepA==null){
                        loser="A";
                    }else{
                        loser="B";
                    }
                }finally{
                    lock.unlock();
                }
                sendResult();
                break;
            }

        }
    }
}
