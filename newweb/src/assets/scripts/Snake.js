

import { AcGameObject } from './AcGameObject';
import { Cell } from './Cell';

export class Snake extends AcGameObject {
    constructor(info, gamemap) {
        super();


        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c)]; //存放蛇的身体，cells[0]存放蛇头
        this.next_cell = null;//下一步的目的地
        this.speed = 5;//蛇每秒走5个格子
        this.direction = -1;//-1表示没有指令，0123表示上右下左
        this.status = "idle";//蛇的状态，蛇的状态可有三种,idle:表示静止，move:表示正在移动，die表示死亡

        //偏移量
        this.dr = [-1, 0, 1, 0];// 4个方向行的偏移量
        this.dc = [0, 1, 0, -1];// 4个方向的列的偏移量

        this.step = 0;//用来定义回合数
        this.eps = 1e-2;//允许的误差

        this.eye_direction = 0;//定义初始的眼睛的位置方向
        if (this.id === 1) this.eye_direction = 2;//第二条默认朝下




        //不同方向蛇眼睛的偏移量
        this.eye_dx = [
            //x的偏移量
            [-1, 1],//朝上
            [1, 1],//朝右
            [1, -1],//朝下
            [-1, -1],//朝左

        ];
        this.eye_dy = [
            //y的偏移量
            [-1, -1],//上
            [-1, 1],//右
            [1, 1],//下
            [1, -1],//左
        ]



    }



    start() {

    }

    // 统一的接口用来设置方向
    set_direction(d) {
        this.direction = d;


    }

    //辅助函数判断蛇尾是否增加
    check_tail_increasing() {
        // 检测当前回合蛇的长度是否增加
        if (this.step <= 10) return true;//前10个回合的话一定变长
        //后续没三步长一步
        if (this.step % 3 === 1) return true;
        return false;
    }

    next_step() {//此为下一步的状态函数
        // 将蛇的状态变为走下一步
        const d = this.direction;// 取出当前的方向
        // 下一步的位置
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.eye_direciton = d;//让蛇眼睛的方向与头的运动方向一致
        this.direction = -1;// 清空方向
        this.status = "move";// 此时在找到位置之后更改下状态为move
        this.step++;// 每次进入到下一回合，回合数++

        // 抛出一个新球
        const k = this.cells.length;
        //从最后面(头)到最前面(尾巴)把每一个小球都向后移动一位
        for (let i = k; i > 0; i--) {
            // this.cells[i]=this.cells[i-1]//每个小球向后移动一位,这样是引用写法，会出现混淆
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
        ///如果说下一步的状态为会被撞到蛇尾或者说障碍物，应该判断直接死亡
        if (!this.gamemap.check_valid(this.next_cell)) {
            this.status = "die";

        }


    }
    update_move() {
        //this.cells[0].x+=this.speed*this.timedelta/1000;//求得的是移动距离，由于帧与帧之间不是1s，timedelta有求相邻两个帧之间的时间间隔，即可
        //this.cells[0].y-=this.speed*this.timedelta/1000;
        //const move_distance=this.speed*this.timedelta/1000;//每一帧的移动距离
        //求出dx与dy
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        //偏移距离
        const distance = Math.sqrt(dx * dx + dy * dy);//求曼哈顿距离

        //计算有没有走到终点,在误差以内重合算作走到,移动到目标点应该是停下来了
        if (distance < this.eps) {
            this.cells[0] = this.next_cell;//将头节点更换成目标节点
            this.next_cell = null;//将目标节点清空
            this.status = "idle";//修改为停止状态
            if (!this.check_tail_increasing()) {
                this.cells.pop();//如果蛇不变长，把尾巴砍掉
            }

        }
        //如果没有就继续移动
        else {
            const move_distance = this.speed * this.timedelta / 1000;//每一帧的移动距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if (!this.check_tail_increasing()) {
                //如果蛇尾不变长的话，就要去移动，这样蛇所谓向后移动，也就是下一个目的地
                const k = this.cells.length;//蛇的整体长度
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];//取出蛇尾
                //把蛇尾移动到目标位置
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }
    update() {
        //每一帧执行一次

        //update_move()只有在移动状态下可执行
        if (this.status === 'move') {
            this.update_move();
        }
        this.render();//每帧重新画一遍

    }
    render() {
        const L = this.gamemap.L;//取出每一个单元格的长度
        const ctx = this.gamemap.ctx;//取出画布的引用

        ctx.fillStyle = this.color;
        //标注蛇的死亡
        if (this.status === "die")
            ctx.fillStyle = "white";
        //枚举蛇的所有身体，一条蛇不止有一个身体
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L * 0.8 / 2, 0, Math.PI * 2);//画圆弧,前两个是每一个小圆的终点。下一个是圆的半径，最后的两个是画圆弧的起始角度和终止角度)
            ctx.fill();
        }
        //枚举一条蛇的相邻的两个圈，来做填充
        for (let i = 1; i < this.cells.length; i++) {
            //i的圈的相邻的两个圈，一个记为a，一个记为b
            const a = this.cells[i - 1], b = this.cells[i];
            //如果在运动过程中两个球已经重合，则不需要再画
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            //竖直方向的画法
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);//分别是左上角的坐标与矩形的长宽,求相对距离，因此要乘以L
            }
            //横方向的画法   
            else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);

            }
        }

        ctx.fillStyle = "black";
        //枚举所有眼睛,i枚举左眼右眼
        //console.log(this.eye_x, this.eye_y);
        for (let i = 0; i < 2; i++) {
            //console.log(this.g);
            //console.log(this.eye_dx[2][i]);
            // console.log(this.eye_direciton);
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;//眼睛的横坐标
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;//眼睛的纵坐标

            ctx.beginPath();//画圆
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);//分别是圆的中心点，半径，起始角度，终止角度
            ctx.fill();
        }
        // var _this=this;
        /*
        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i++) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;

            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
*/
    }
}