package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    //存一下BotId与BotCode
    private Integer botId;//-1表示个人操作，否则表示用AI操作
    private String botCode;
    private Integer sx;
    private Integer sy;
    private List<Integer>steps;
    //辅助函数，以检验当前回合蛇的长度是否增加
    private boolean check_tail_increasing(int step){
        if(step<=10) return true;
        return step%3==1;
    }
    //列表存储方向与起点终点以确定路径
    public List<Cell>getCells(){
        List<Cell>res=new ArrayList<>();//初始化一下
        //定义偏移量，但是这个偏移量的顺序不要变
        int [] dx={-1,0,1,1},dy={0,1,0,-1};

        int x=sx,y=sy;

        int step=0;//定义步数，记录当前计数步
        //初始化添加起点
        res.add(new Cell(x,y));

        //枚举下每一步
        for(int d:steps) {
            //通过偏移量，赋值新的下一步
            x += dx[d];
            y += dy[d];

            //将要走的下一步坐标加进函数里
            res.add(new Cell(x, y));
            //当前的计数步下，如果蛇尾是不增加的情况，那么就把蛇尾给删掉
            if(!check_tail_increasing(++step)){
                 res.remove(0);
            }

        }
        return res;
    }

    //定义函数，用来将String类型转化成字符串
    public String getStepsString(){
        StringBuilder res=new StringBuilder();
        //枚举下每一个steps
        for(int d:steps){
            res.append(d);//每一步都转化成字符串追加到结果集中

        }
        return res.toString();
    }
}
