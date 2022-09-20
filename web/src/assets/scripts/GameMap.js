

import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject{
    constructor(ctx,parent){
        super();

        this.ctx=ctx;
        this.parent=parent;
        this.L=0;// 绝对距离，L表示一个单位的长度

        //行数和列数
        this.rows=13;
        this.cols=13;

        this.inner_walls_count=60;//记录下内置障碍物的数量
        //开行数组用来存储所有的墙
        this.walls=[];



    }
    //创建函数判断障碍物是否为联通的
    // 首先先传入地图，起点坐标，终点坐标
    check_connectivity(g,sx,sy,tx,ty){
        if(sx==tx&&sy==ty)return true;///如果已经返回终点说明是联通的，返回true
        g[sx][sy]=true;//初始化起点为已经走过
        //标记偏移量
        let dx=[-1, 0, 1,0],dy=[0,1,0,-1];
        //枚举四个方位，判断是否联通
        for(let i=0;i<4;i++){
            let x=sx+dx[i],y=sy+dy[i];//定义偏移方向的下一个点
            if(!g[x][y]&&this.check_connectivity(g,x,y,tx,ty))
               return true;//如果当前不会撞墙的同时相邻点之间是连通的，返回true
        }
        return false;


    }

    //创建辅助函数,以创建墙/障碍物
    create_walls(){
        //new Wall(0,0,this);
         const g=[];//bool数组，用来判断 某个方块的位置上是否有墙
         for(let r=0;r<this.rows;r++){
            g[r]=[];
            for(let c=0;c<this.cols;c++){
                g[r][c]=false;//初始化的情况下将所有的置为false
            }
         }
         //首先先要四周有一圈墙
        //首先先给左右两边加上墙
         for(let r=0;r<this.rows;r++){
              g[r][0]=g[r][this.cols-1]=true;
         }
         //其次给上下两行加上墙
         for(let c=0;c<this.cols;c++){
            g[0][c]=g[this.rows-1][c]=true;
         }

        //再其次随机放置障碍物，由于每次放两个，所以要除以2
        for(let i=0;i<this.inner_walls_count/2;i++){
            /*if(g[r][c]!=true){
                g[r][c]=true;
            }*/
            //写个死循环1000次，无论如何一个障碍物也能找到空位放下去
            for(let j=0;j<1000;j++){
                let r=parseInt(Math.random()*this.rows);//行的随机值
                let c=parseInt(Math.random()*this.cols);//列的随机值 
                if(g[r][c]||g[c][r])continue;//此位置如果已经有障碍物则跳过即可(由于是对称放的，且在这个枚举的时候，为了简化枚举只枚举一半，但判断一半另一半也要表示)
                if(r==this.rows-1&&c==1||r==1&&c==this.cols-2){
                         continue;
                }//越过左上角和右下角，因为这是蛇的起始位置
                g[r][c]=g[c][r]=true;
                break;//每一个障碍物放下去一次break掉，再去下一个障碍物开始找

            }

        }
        const copy_g=JSON.parse(JSON.stringify(g));//将对象深度复制并且解析,以存储对象当前状态

        if(!this.check_connectivity(copy_g,this.rows-2,1,1,this.cols-2))return false;//分别是起点横坐标与终点横坐标(以左下半边的蛇为例)

         //在加完之后再枚举一整个数组
         for(let r=0;r<this.rows;r++){
            for(let c=0;c<this.cols;c++){
                if(g[r][c]==true){
                    this.walls.push(new Wall(r,c,this));
                }
            }
         }
         return true;
    }

    start(){

        //只执行一次
        for(let i=0;i<1000;i++)
           if(this.create_walls())
               break;
    }
    update_size(){
        //每一帧都更新一下边长
        this.L=parseInt(Math.min(this.parent.clientWidth / this.cols,this.parent.clientHeight/this.rows));
        //求canvas的长度(也就是哪个最大的正方形)
        this.ctx.canvas.width=this.L*this.cols;//求的是canvas的长
        this.ctx.canvas.height=this.L*this.rows;//求得canvas的宽
    }

    update(){
        //每一帧执行一次
        this.update_size();
        this.render();
    }

    render(){
        // update的辅助函数
        // 先画一下地图
       /* this.ctx.fillStyle='green';
        this.ctx.fillRect(0,0,this.ctx.canvas.width,this.ctx.canvas.height);*/
        //写棋盘
        const color_even="#AAD751",color_odd="#A2D149";
        // r枚举行，c枚举列
        for(let r=0;r<this.rows;r++){
            for(let c=0;c<this.cols;c++){
                if((r+c)%2==0){
                    this.ctx.fillStyle=color_even;
                }else{
                    this.ctx.fillStyle=color_odd;
                }
                 //画出小正方形
                this.ctx.fillRect(c*this.L,r*this.L,this.L,this.L);

            }


        }
       
    }
}