<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.loser=='all'">
            Draw
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser==='A' &&$store.state.pk.a_id==$store.state.user.id">
          lose
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser==='B'&&$store.state.pk.b_id==$store.state.user.id">
        lose
        </div>
        <div class="result-board-text" v-else>
            win
        </div>
        <div class="result-board-btn">
            <button @click="restart" type="button" class="btn btn-light btn-lg">
                重开一局

            </button>
        </div>

    </div>    
</template>
<script>
import {useStore} from 'vuex';
export default{
    setup(){
        const store=useStore();
        const restart =()=>{
            store.commit("updateStatus","matching");//重新更新界面
            store.commit("updateLoser","none");//重新更新胜负状态
            //变化头像
            store.commit("updateOpponent",{
        username:"未知对手",
        photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
      })

        }
        return {
            restart,
        };
    }
}

</script>

<style scoped>
div.result-board{
    height:30vh;
    width:30vw;
    background-color:rgba(50,50,50,0.5);
    position:absolute;
    top:23vh;
    left:35vw;
}
div.result-board-text{
    text-align:center;
    font-size:50px;
    font-weight:600;
    font-style:italic;
    padding-top:5vh;

    color:white;
}
div.result-board-btn{
    text-align:center;
    padding-top: 7vh;
    
}
</style>