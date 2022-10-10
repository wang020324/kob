<template>
  <!---<div>
    <div>Bot昵称:{{ bot_name }}</div>
    <div>Bot战力:{{bot_rating}}</div>
  </div>-->
  <NavBar />
  <router-view/>
</template>
<script>

//import $ from 'jquery';
//import { ref } from 'vue';
import NavBar from './components/NavBar.vue';
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
//import $ from 'jquery'


export default {
  //连接后端
  /*name:"App",
  setup:() =>{
    let bot_name=ref("");
    let bot_rating=ref("");

    $.ajax({
      url:"http://127.0.0.1:8080/pk/getbotinfo/",
      type:"get",
      success:resp=>{
        bot_name.value=resp.name;
        bot_rating.value=resp.rating;
        console.log(resp);
      }
    })

    return {
      bot_name,
      bot_rating
    }
    
  }*/
  components: {
    NavBar,
 },
 setup(){
  //第一个api，输入用户名与密码
/*    $.ajax({
      url:"http://127.0.0.1:3000/user/account/token/",
      type:"post",
      data:{
        //放入用户名和密码进行登录测试
        username:"void",
        password:"pass",


      },
    success(resp){
      console.log(resp);
    },
    error(resp){
      console.log(resp);
    }

    });
    //第二个api,读取用户信息

    $.ajax({
      url:"http://127.0.0.1:3000/user/account/info/",
      type:"get",
      headers:{
         Authorization:"Bearer "+"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhNGJkYTQwYmM1MWQ0OTc2ODYzMzA0YzVmY2VhMGJhNyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2NTE5Nzc1MiwiZXhwIjoxNjY2NDA3MzUyfQ.Sr5cYHYHH7QGy3JPTWENV_1kl6ogKKJQkrSbEfvD3V0",
      },
     success(resp){
      console.log(resp);
     },
     error(resp){
        console.log(resp);
      }
    });

    //第三段api,注册页面
    $.ajax({
      url:"http://127.0.0.1:3000/user/account/register/",
      type:"post",
      data:{
        username:"yxc2",
        password:"123",
        confirmedPassword:"123",
      },
      success(resp){
        console.log(resp);
      },
      error(resp){
        console.log(resp);
      }

    });*/
   }
}
</script>

<style>
 body{
  background-image:url("./assets/images/background.png");
  background-size: cover;
 }
</style>
