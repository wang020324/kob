package com.kob.newbotrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

public class Bot implements com.kob.newbotrunningsystem.utils.BotInterface {
    //更高级的AI
    //创建Cell以存储每条蛇的所有的位置
    static class Cell {
        public int x, y;//蛇的位置

        //定义构造函数
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    //取出蛇的身体
    private boolean check_tail_increasing(int step) {
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    //列表存储方向与起点终点以确定路径
    public List<Cell> getCells(int sx, int sy, String steps) {
        steps =steps.substring(1,steps.length()-1);//去掉之前编码的括号
        List<Cell> res = new ArrayList<>();//初始化一下
        //定义偏移量，但是这个偏移量的顺序不要变
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

        int x = sx, y = sy;

        //初始化添加起点
        int step = 0;//定义步数，记录当前计数步
        res.add(new Cell(x, y));

        //枚举下每一步
        for (int i = 0; i < steps.length(); i++) {
            //定义方向坐标
            int d = steps.charAt(i) - '0';//用'0'将其转化成一个整数
            //通过偏移量，赋值新的下一步
            x += dx[d];
            y += dy[d];

            //将要走的下一步坐标加进函数里
            res.add(new Cell(x, y));
            //当前的计数步下，如果蛇尾是不增加的情况，那么就把蛇尾给删掉
            if (!check_tail_increasing(++step)) {
                res.remove(0);
            }

        }
        return res;
    }

    @Override
    public Integer nextMove(String input) {
        //解码字符串
        String[] strs = input.split("#");
        //定义地图
        int[][] g = new int[13][14];
        //首先先初始化地图
        for (int i = 0, k = 0; i < 13; i++) {
            for (int j = 0; j < 14; j++, k++) {
                if (strs[0].charAt(k) == '1') {
                    g[i][j] = 1;//初始标注蛇头
                }
            }

        }
        //取出两条蛇的身体
        //首先先取出两条蛇的起点
        int aSx=Integer.parseInt(strs[1]),aSy=Integer.parseInt(strs[2]);
        int bSx=Integer.parseInt(strs[4]),bSy=Integer.parseInt(strs[5]);

        //获得两条蛇的操作
        List<Cell>aCells=getCells(aSx,aSy,strs[3]);
        List<Cell>bCells=getCells(bSx,bSy,strs[6]);

        //标注两条蛇的身体
        for(Cell c:aCells)g[c.x][c.y]=1;
        for(Cell c:bCells)g[c.x][c.y]=1;

        //枚举上下左右四个方向，判断哪个方向可以走
        int[] dx={-1,0,1,0},dy={0,1,0,-1};//定义便宜量
        for(int i=0;i<4;i++){
            //首先先取出整个蛇身的最后一位元素
            int x=aCells.get(aCells.size()-1).x+dx[i];
            int y=aCells.get(aCells.size()-1).y+dy[i];
            if(x>=0&&x<13&&y>=0&&y<14&&g[x][y]==0){
                return i;
            }

       }
        return 0;
   }
    /*@Override
    public Integer nextMove(String input) {
        return 0;//0表示向上走
    }*/

}
