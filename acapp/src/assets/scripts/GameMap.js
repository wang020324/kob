

import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";
import { Snake } from "./Snake";
export class GameMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0;// 绝对距离，L表示一个单位的长度

        //行数和列数
        this.rows = 13;
        this.cols = 14;

        this.inner_walls_count = 60;//记录下内置障碍物的数量
        //开行数组用来存储所有的墙
        this.walls = [];

        this.snakes = [
            new Snake({ id: 0, color: "#4876EC", r: this.rows - 2, c: 1 }, this),
            new Snake({ id: 1, color: "#F94848", r: 1, c: this.cols - 2 }, this),
        ];


    }
    //创建函数判断障碍物是否为联通的
    // 首先先传入地图，起点坐标，终点坐标
    /*check_connectivity(g, sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true;///如果已经返回终点说明是联通的，返回true
        g[sx][sy] = true;//初始化起点为已经走过
        //标记偏移量
        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        //枚举四个方位，判断是否联通
        for (let i = 0; i < 4; i++) {
            let x = sx + dx[i], y = sy + dy[i];//定义偏移方向的下一个点
            if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty))
                return true;//如果当前不会撞墙的同时相邻点之间是连通的，返回true
        }
        return false;


    }*/

    //创建辅助函数,以创建墙/障碍物
    create_walls() {
        const g = this.store.state.pk.gamemap;//取出地图
        //new Wall(0,0,this);
        //const g = [];//bool数组，用来判断 某个方块的位置上是否有墙
        /* for (let r = 0; r < this.rows; r++) {
             g[r] = [];
             for (let c = 0; c < this.cols; c++) {
                 g[r][c] = false;//初始化的情况下将所有的置为false
             }
         }*/
        //首先先要四周有一圈墙
        //首先先给左右两边加上墙
        /*for (let r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = true;
        }*/
        //其次给上下两行加上墙
        /*for (let c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = true;
        }*/

        //再其次随机放置障碍物，由于每次放两个，所以要除以2
        // for (let i = 0; i < this.inner_walls_count / 2; i++) {
        /*if(g[r][c]!=true){
            g[r][c]=true;
        }*/
        //写个死循环1000次，无论如何一个障碍物也能找到空位放下去
        /*   for (let j = 0; j < 1000; j++) {
               let r = parseInt(Math.random() * this.rows);//行的随机值
               let c = parseInt(Math.random() * this.cols);//列的随机值 
               //if(g[r][c]||g[c][r])continue;//此位置如果已经有障碍物则跳过即可(由于是对称放的，且在这个枚举的时候，为了简化枚举只枚举一半，但判断一半另一半也要表示)
               //修改对称模式
               if (g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue;
               if (r == this.rows - 1 && c == 1 || r == 1 && c == this.cols - 2) {
                   continue;
               }//越过左上角和右下角，因为这是蛇的起始位置
               g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
               break;//每一个障碍物放下去一次break掉，再去下一个障碍物开始找

           }

       }
       const copy_g = JSON.parse(JSON.stringify(g));//将对象深度复制并且解析,以存储对象当前状态

       if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) return false;//分别是起点横坐标与终点横坐标(以左下半边的蛇为例)
*/
        //在加完之后再枚举一整个数组
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
        //return true;
    }

    //辅助函数捕获输入信息
    add_listening_events() {
      //  console.log(this.store.state.record);

        //判断是要对战还是要播放录像，如果是对战的话，则将对战窗口聚焦并且接收输入
        if (this.store.state.record.is_record) {
            let k = 0;//第几步


            //取出移动操作steps
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const loser = this.store.state.record.record_loser;
            //取出两条蛇的信息c
            const [snake0, snake1] = this.snakes;
            //设定多久的时间执行这个函数
            const interval_id = setInterval(() => {
                //每300毫秒先判断蛇有无走完
                if (k >= a_steps.length - 1) {
                    //如果是已经结束的话，那么把死亡的蛇标记，如果没有死亡两条蛇动
                    if (loser === "all" || loser === "A") {
                        snake0.status = "die";
                    }
                    if (loser === "all" || loser === "B") {
                        snake1.status = "die";
                    }
                    //store.commit("updateLoser",data.loser);
                    clearInterval(interval_id);
                } else {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++;
            }, 300)
        } else {
            this.ctx.canvas.focus();
            // 取出两条蛇
            // const [snake0, snake1] = this.snakes;
            //函数控制移动
            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1;//以表示方向，其中d=0为上，d=1为右 2为下 3为左
                /*
                if (e.key === 'w') snake0.set_direction(0);
                else if (e.key === 'd') snake0.set_direction(1);
                else if (e.key === 's') snake0.set_direction(2);
                else if (e.key === 'a') snake0.set_direction(3);
                /*
                else if (e.key === 'ArrowUp') snake1.set_direction(0);
                else if (e.key === 'ArrowRight') snake1.set_direction(1);
                else if (e.key === 'ArrowDown') snake1.set_direction(2);
                else if (e.key === 'ArrowLeft') snake1.set_direction(3);
            */
                if (e.key === 'w') d = 0;
                else if (e.key === 'd') d = 1;
                else if (e.key === 's') d = 2;
                else if (e.key === 'a') d = 3;
                //如果进行了一个合法的移动操作
                if (d >= 0) {
                    //则想后端传递一个请求
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                    }));
                }
            })

        }



    }

    start() {

        //只执行一次
        /*for (let i = 0; i < 1000; i++)
            if (this.create_walls())
                break;*/
        this.create_walls();
        this.add_listening_events();//执行时调用
    }
    update_size() {
        //每一帧都更新一下边长
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        //求canvas的长度(也就是哪个最大的正方形)
        this.ctx.canvas.width = this.L * this.cols;//求的是canvas的长
        this.ctx.canvas.height = this.L * this.rows;//求得canvas的宽
    }
    //用来判断两个蛇是不是准备好下一回合
    check_ready() {
        for (const snake of this.snakes) {
            //蛇的状态要么死了要么移动，就需要把当前位置走完。
            if (snake.status !== "idle") return false;
            //如果这条蛇没有接到下一步的指令的话，return false
            if (snake.direction === -1) return false;// js中判断相等用三个等号
        }
        return true;
    }

    next_step() {
        //让两条蛇进入下一回合
        for (const snake of this.snakes) {
            snake.next_step();

        }

    }

    // 检测撞墙的函数
    //裁判:检测下目标格子是否合法,所谓合法:没有撞到某条蛇的身体和障碍物
    check_valid(cell) {
        //枚举下所有的障碍物:
        for (const wall of this.walls) {
            // 当蛇与身体的横纵坐标相等表示非法
            if (wall.r === cell.r && wall.c === cell.c)
                return false;
        }
        //枚举两条蛇的身体，判断有无重合

        for (const snake of this.snakes) {
            //蛇尾要单独特判会不会缩,来判断一条蛇会不会撞在一起
            let k = snake.cells.length;//先取出蛇的长度
            //如果要缩，最后一个格子就可以走
            if (!snake.check_tail_increasing()) {
                //当辅助函数判断不会变长，说明要缩，就可以走
                k--;
            }
            for (let i = 0; i < k; i++) {
                //枚举整条蛇，如果撞了的话返回false
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;

            }
        }
        return true;



    }

    update() {
        //每一帧执行一次
        this.update_size();
        //当两条蛇都已经准备好的时候，两条蛇都可以进入下一回合
        if (this.check_ready()) {
            this.next_step();

        }
        this.render();
    }

    render() {
        // update的辅助函数
        // 先画一下地图
        /* this.ctx.fillStyle='green';
         this.ctx.fillRect(0,0,this.ctx.canvas.width,this.ctx.canvas.height);*/
        //写棋盘
        const color_even = "#AAD751", color_odd = "#A2D149";
        // r枚举行，c枚举列
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                //画出小正方形
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);

            }


        }

    }
}