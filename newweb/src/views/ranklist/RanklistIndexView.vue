<template>
    <ContentField>
      <table class="table table-striped table-hover">
                    <thead>
                      <tr style="text-align:center">
                        <th >玩家</th>
                        <th>天梯分</th>

                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="user in users" :key="user.id">
                        <td>
                           <img :src="user.photo" alt="" class="record-user-photo">
                           &nbsp;
                           <span class="record-user-username">{{ user.username }}</span>
                        </td>
                        
                        <td>{{ user.rating }}</td>
                       
                         
                      </tr>
                    </tbody>
                  </table>
                  <nav aria-label="Page navigation example">
                    <ul class="pagination" style="float:right;">
                      <li class="page-item">
                        <a class="page-link" href="#" @click="click_page(-2)">&laquo;</a>
                     </li>
                     <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                        <a class="page-link" href="#">{{ page.number }}</a>
                     </li>
                  
                      <li class="page-item">
                        <a class="page-link" href="#" @click="click_page(-1)">&raquo;</a>
                     </li>
                   </ul>
</nav>
    </ContentField>
</template>

<script>
import ContentField from "../../components/ContentField.vue";
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';

export default{
   components:{
       ContentField,
   },
   setup(){
    const store=useStore();
    let users =ref([]);
   let current_page=1;//定义当前页面
   let total_users=0;//存储当前的对局数量
   let pages=ref([]);//全局变量数组,在里面存储页面跳转的相关信息

  // console.log(total_records);//定义的变量一定要使用否则会报ce


   

   //辅助函数用来当点击对应的页码跳转到对应的页面

   const click_page =page =>{
      if(page === -2) page =current_page-1;
      else if(page === -1) page =current_page+1;
      let max_pages=parseInt(Math.ceil(total_users));

      //如果范围合法，定义一个新的分页
        if(page>=1&&page<=max_pages){
            pull_page(page);
        }
   }

   const update_pages = () =>{
      let max_pages=parseInt(Math.ceil(total_users));
      let new_pages =[];
      //console.log(max_pages);

      for(let i=current_page -2 ;i<=current_page+2;i++){
        if(i>=1&&i<=max_pages){
            new_pages.push({
               number:i,
               is_active: i === current_page ? "active" : "",
            });
         }
         
      }
        pages.value=new_pages;
   }
    
   const pull_page=page=>{
        current_page=page;//打开的页面为当前页面
        $.ajax({
             url:"http://127.0.0.1:3000/ranklist/getlist/",
             type:"GET",
             data:{
                page,
             },
             headers:{
                Authorization:"Bearer "+store.state.user.token,
             },
             success(resp){
                console.log(resp);
                users.value=resp.users;
                total_users=resp.users_count;
                //返回完信息之后都需要更新页面以更新页码
                update_pages();
             },
             error(resp){
                console.log(resp);
             }
        })
    }

    pull_page(current_page);
    
   
    
    return{
        users,
      
        pages,
        click_page,



    }
   }
}


</script>

<style scoped>

img.record-user-photo{
   width:5vh;
   border-radius: 50%;
}

</style>