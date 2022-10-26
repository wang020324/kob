<template>
     <PlayGround v-if="$store.state.pk.status ==='playing'" />
   
        <!--对战-->
     <MatchGround v-if="$store.state.pk.status === 'matching'" />
    <!--匹配-->
</template>

<script>
//import ContentField from '../../components/ContentField.vue'
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import { onMounted ,onUnmounted} from 'vue'
import { useStore } from 'vuex'

export default{
   components:{
    // ContentField,
    PlayGround,
    MatchGround,
  
},
  setup(){

    const store=useStore();
    const socketUrl=`ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
    
    let socket =null;

    onMounted(()=>{
      store.commit("updateOpponent",{
        username:"未知对手",
        photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
      })
      socket = new WebSocket(socketUrl);

      socket.onopen =()=>{
        console.log("connected!");
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
          store.commit("updateGamemap",data.gamemap);
         
        }
      }
      socket.onclose =()=>{
        console.log("disconnected!");
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

</style>