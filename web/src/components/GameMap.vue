<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0">

        </canvas>
    </div>
</template>


<script>
import {GameMap} from '@/assets/scripts/GameMap';
import { ref,onMounted } from 'vue' //记得引入onMounted

export default{
    setup(){
        let parent=ref(null);//定义的div
        let canvas=ref(null);
        onMounted(()=>{
            // 组件挂载完之后需要创建GameMap对象    
            new GameMap(canvas.value.getContext('2d'),parent.value)

        });
        return {
            parent,
            canvas,
        }
    }
    
}
</script>
<style scoped>
    div.gamemap{
        width:100%;
        height:100%;
        display:flex;
        align-content: center;
        justify-content: center; 
    }
</style>