<template>
    
     <ContentField>
      <PlayGround v-if="$store.state.pk.status ==='playing'" />
   
       <!--对战-->
      <MatchGround v-if="$store.state.pk.status === 'matching'" />
       <!--匹配-->
      <ResultBoard v-if="$store.state.pk.loser !='none'"/>
        <!--结果展示-->
      <div class="user-color" v-if="$store.state.pk.status ==='playing' &&parseInt($store.state.user.id) === parseInt($store.state.pk.a_id)">您的初始位置：左下角</div>
      <div class="user-color" v-if="$store.state.pk.status==='playing'&& parseInt($store.state.user.id) ===parseInt($store.state.pk.b_id)" >您的初始位置:右上角</div>
<!--标记bot的位置-->
     </ContentField>
     
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import ResultBoard from '../../components/ResultBoard.vue'
import { onMounted ,onUnmounted} from 'vue'
import { useStore } from 'vuex'

export default{
   components:{
     ContentField,
    PlayGround,
    MatchGround,
    ResultBoard,
    
  
},
  setup(){

    const store=useStore();
    const socketUrl=`wss://app2753.acapp.acwing.com.cn/websocket/${store.state.user.token}/`;
    store.commit("updateLoser","none");//重新更新胜负状态 
    store.commit("updateIsRecord",false);//更新记录页面的状态
    let socket =null;

    onMounted(()=>{
      store.commit("updateOpponent",{
        username:"未知对手",
        photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
      })
      socket = new WebSocket(socketUrl);

      socket.onopen =()=>{
        //console.log("connected!");
        store.commit("updateSocket",socket);
      }
      socket.onmessage = msg =>{
        const data =JSON.parse(msg.data);
        if(data.event ==="start-matching"){
          
          //当匹配成功时
          store.commit("updateOpponent",{
            username:data.opponent_username,
            photo:data.opponent_photo,
          });
          //添加时间戳以增加等待时间
          setTimeout(()=>{
            store.commit("updateStatus","playing");
          },2000);
          store.commit("updateGame",data.game);
         
        }else if(data.event==="move"){
               //console.log(data);
                //获取出来GameObject
                const game=store.state.pk.gameObject;
                //解构GameMap的snakes属性
                const[snake0,snake1]=game.snakes;
                //获取到两条蛇的移动方向
                snake0.set_direction(data.a_direction);
                snake1.set_direction(data.b_direction);
        }else if(data.event==="result"){
             //console.log(data);
             const game =store.state.pk.gameObject;
             const[snake0,snake1]=game.snakes;
             if(data.loser==="all"||data.loser==="A"){
              snake0.status="die";
             }
             if(data.loser==="all"||data.loser==="B"){
              snake1.status="die";
             }
             store.commit("updateLoser",data.loser);
        }
      }
      socket.onclose =()=>{
       // console.log("disconnected!");
      }
    });
    onUnmounted(()=>{
      socket.close();
      store.commit("updateStatus","matching");
    })
  }
}
</script>

<style scoped>
div.user-color{
  position:absolute;
  bottom:5vh;
  width:100%;
  text-align:center;
  color:white;
  font-size:30px;
  font-weight:600;
}
</style>