import { AcGameObject } from "./AcGameObject";

export class Wall extends AcGameObject {
    constructor(r,c,gamemap){
        //传入横纵坐标与整个游戏地图
        super();
        
        this.r=r;
        this.c=c;
        this.gamemap=gamemap;
        this.color="#B37226";//定义下墙的颜色
    }
    update(){

        this.render();


    }
    render(){
       const L=this.gamemap.L;//由于L是动态变化的因此需要动态取，L是单位小正方格的边长
       const ctx=this.gamemap.ctx;//取过来引用的canvas
       ctx.fillStyle=this.color;
       ctx.fillRect(this.c*L,this.r*L,L,L);
    }
}