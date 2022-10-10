const AC_GAME_OBJECTS = [];

export class AcGameObject{
    constructor(){
        AC_GAME_OBJECTS.push(this);
        this.timedelta = 0;//存储这一帧的执行时刻与上一帧的执行时刻的时间间隔 
        this.has_called_start=false;//记录下有没有执行过
    }

    start(){
        // 只执行一次，创建时执行

    }

    update(){
        // 除了第一次之外，每一帧执行一次
    }

    on_destroy(){
        // 删除之前的一些回调函数
       
    }

    destroy(){
        this.on_destroy();// 删除前调用函数执行回调函数
        // 从数组中删除对象
        for(let i in AC_GAME_OBJECTS){
            const obj=AC_GAME_OBJECTS[i];// 取出当前对象
            // 取出的对象等于当前对象则删除
            if(obj === this){
                  AC_GAME_OBJECTS.splice(i);
            }
        }
    }
}

let last_timestamp; // 上一次执行的时刻 

const step=timestamp=>{
    for (let obj of AC_GAME_OBJECTS){
        //如果这个对象没有执行过start函数的话,执行start函数,否则执行update函数
        if(!obj.has_called_start){
            obj.has_called_start=true;//表示已经执行过
               obj.start();
        }else{
            obj.timedelta=timestamp-last_timestamp;
            obj.update();

        }
    }
    last_timestamp=timestamp;
    // 下一帧执行stamp函数
    requestAnimationFrame(step)
}
requestAnimationFrame(step)