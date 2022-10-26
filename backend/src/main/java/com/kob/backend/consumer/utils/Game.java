package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {
    //先定义一些私有变量
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;//墙的数量
    final private int[][] g;//整个地图
    final private static int[] dx={-1,0,1,0},dy={0,1,0,-1};//放障碍物的偏移量
    //构造函数
    public Game(Integer rows,Integer cols,Integer inner_walls_count){
        this.rows=rows;
        this.cols=cols;
        this.inner_walls_count=inner_walls_count;
        this.g =new int[rows][cols];
    }
    //生成地图
    public int[][] getG(){
        return g;
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
            //循环1009次以随机生成
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
}
