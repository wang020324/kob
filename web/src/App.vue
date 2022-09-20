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


export default {
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
}
}
</script>

<style>
 body{
  background-image:url("./assets/images/background.png");
  background-size: cover;
 }
</style>
