<template>
  <div class="content-field">
    <div class="playground">
        <GameMap />
        <div class="go-back" @click="click_go_back_handler">
        返回ESC
     </div>
    </div>
  </div>
    
</template>

<script>
import GameMap from "./GameMap.vue";
import { useStore } from "vuex";

export default{

  components:{
    GameMap,
  },
    setup(){
        const store=useStore();

        const click_go_back_handler=()=>{
            store.commit("updateRouterName","record");
        }
        return{
            click_go_back_handler,
        }
    }
}
</script>

<style scoped>
  div.playground{
    width:40vw;
    height:50vh;

  }
  div.content-field{
    display:flex;
    justify-content:center;
    align-items:center;
    width:100%;
    height:100%;
  }
  div.go-back{
        position:absolute;
        left:5vh;
        bottom:5vh;
        color:white;
        font-size:24px;
        font-style:italic;
        font-weight:600;
        cursor:pointer;
        user-select:none;

    }
    div.go-back:hover{
        transform:scale(1,2);
        position:200ms;
    }
</style>