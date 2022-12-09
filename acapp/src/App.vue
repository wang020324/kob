<template>


  <div class="game-body">
    <MenuView  v-if="$store.state.router.router_name=== 'menu'" />
    <PkIndexViewVue v-else-if="$store.state.router.router_name==='pk'"/>
    <RecordContentViewVue v-else-if="$store.state.router.router_name ==='record_content'"/>
    <RecordIndexViewVue v-else-if="$store.state.router.router_name ==='record'"/>
    <RanklistIndexViewVue v-else-if="$store.state.router.router_name ==='ranklist'"/>
    <UserBotIndexViewVue  v-else-if="$store.state.router.router_name ==='user_bot'"/>

    </div>



 


  
 
  
  
    

  


</template>
<script>



import {useStore} from 'vuex';
import  MenuView from './views/MenuView.vue';
import PkIndexViewVue from "./views/pk/PkIndexView.vue";
import RecordIndexViewVue from "./views/record/RecordIndexView.vue";
import RecordContentViewVue from "./views/record/RecordContentView.vue";
import RanklistIndexViewVue from "./views/ranklist/RanklistIndexView.vue";
import UserBotIndexViewVue from "./views/user/bot/UserBotIndexView.vue";

//import $ from 'jquery'


export default {
 
  components: {
    MenuView,
    PkIndexViewVue,
    RecordContentViewVue,
    RecordIndexViewVue,
    RanklistIndexViewVue,
    UserBotIndexViewVue, 
   
 },
 setup(){
  const store=useStore();
    // const jwt_token=localStorage.getItem("jwt_token");//取出token
    const jwt_token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1ZDJkOGE1NjU5MzE0ODNiYTgyZTkyOWZjNjQyOTc3MCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2OTQyOTI5OCwiZXhwIjoxNjcwNjM4ODk4fQ.StN89RqrfYdwQDkMWc8uRMmCTi7P1Mj8VHeTip4j4-I";
    //如果token不存在，则返回一个空,存在则存下来
    if(jwt_token){
      store.commit("updateToken",jwt_token);//更新进去新的token去内存
      //验证token是否合法
      store.dispatch("getinfo",{
        success(){
          //成功的话(也就是合法的话)就跳转到首页
       
          store.commit("updatePullingInfo",false);
        },
        error(){
          //show_content.value=true;
          store.commit("updatePullingInfo",false);
        }

      })

    }else
    {
      //show_content.value=true;
      store.commit("updatePullingInfo",false);
    }

   }
}
</script>

<style scoped>

body{
  margin:0;
}
 div.game-body{
  background-image:url("./assets/images/background.png");
  background-size: cover;
  width:100%;
  height:100%;
 }
 div.window{
  width:100vw;
  height:100vh;
 }
</style>
