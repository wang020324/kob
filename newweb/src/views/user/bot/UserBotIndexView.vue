<template>
    <ContentField>
        我的Bot
    </ContentField>
</template>

<script>
import ContentField from "../../../components/ContentField.vue";
import $ from 'jquery';
//import { useStore } from 'vuex';

export default{
    components:{
        ContentField,
    },
    setup(){
      //const store = useStore();
    const jwt_token=localStorage.getItem("jwt_token");//取出token
    /*
     $.ajax({
        url: "http://127.0.0.1:3000/user/bot/add/",
        type: "POST",
        headers: {
         // Authorization:"Bearer "+"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiNTg4ZmFiNWE4MjA0YmRkYWY1ZGY0NzU2YmI4ZGMyMiIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2NTgwNjA0NiwiZXhwIjoxNjY3MDE1NjQ2fQ.2IJAoNjjdecGX34WIAZhUqBAVrI9-UjzeRil3iXvp6w",
          Authorization:"Bearer "+ jwt_token,
        },
        data: {
           title: "Bot的标题",
           description : "Bot的描述",
           content: "Bot的代码",
         },
        
            success(resp) {
                 console.log(resp);
                 console.log(jwt_token);
                //console.log(store.state.user.user_token);
                },
            error(resp) {
               console.log(resp);
             //console.log(store.state.user.user_token);
          }
        })*/
        //第二个api，删除的调试
        /*
        $.ajax({
          url:"http://127.0.0.1:3000/user/bot/remove/",
          type:"POST",
          headers:{
            Authorization:"Bearer "+jwt_token,
          },
          data:{
            bot_id:17
          },
          success(resp){
            console.log(resp);
          },
          error(resp){
            console.log(resp);
          }

        })
        */
       //第三个api
       /*
       $.ajax({
        url:"http://127.0.0.1:3000/user/bot/update/",
        type:"POST",
        data:{
          bot_id:18,
          title:"Bot的标题7",
          content:"Bot的描述7",
          description:"Bot的代码7",
        },
        headers:{
          Authorization:"Bearer "+jwt_token,
        },
        success(resp){
          console.log(resp);
        },
        error(resp){
          console.log(resp);
        }
       })
       */
      //第四个api
      $.ajax({
       url:"http://127.0.0.1:3000/user/bot/getlist/",
       type:"GET",
       headers:{
        Authorization:"Bearer "+jwt_token,
       },
       success(resp){
        console.log(resp);
       },
       error(resp){
        console.log(resp);
       }
      })
      }

}

</script>

<style scoped>

</style>