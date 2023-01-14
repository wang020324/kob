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
import $ from 'jquery'


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
    $.ajax({
      url:"https://app2753.acapp.acwing.com.cn/api/user/account/acwing/acapp/apply_code/",
      type:"GET",
      success:resp=>{
        //apply获取的token为success,申请授权登录
        if(resp.result==="success"){
             store.state.user.AcWingOS.api.oauth2.authorize(resp.appid, resp.redirect_uri, resp.scope, resp.state, resp=>{
              //回调返回的结果为success，申请授权登录
                if(resp.result==="success"){
                  const jwt_token=resp.jwt_token;
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
            }else{
                  //失败关闭窗口
                  store.state.user.AcWingOS.api.window.close();
                }
             });

        }else{
          //一开始拒绝授权也关闭
          store.state.user.AcWingOS.api.window.close();
        }
      }
    });
    // const jwt_token=localStorage.getItem("jwt_token");//取出token

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
