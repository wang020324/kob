<template>
    <ContentField>
      <table class="table table-striped table-hover">
                    <thead>
                      <tr style="text-align:center">
                        <th >A</th>
                        <th>B</th>
                        <th>对战结果</th>
                        <th>对战时间</th>
                        <th>操作</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="record in records" :key="record.record.id">
                        <td>
                           <img :src="record.a_photo" alt="" class="record-user-photo">
                           &nbsp;
                           <span class="record-user-username">{{ record.a_username }}</span>
                        </td>
                        <td>
                           <img :src="record.b_photo" alt="" class="record-user-photo">
                           <span class="record-user-username">{{ record.b_username }}</span>
                        </td>
                        <td>{{ record.result }}</td>
                        <td>{{ record.record.createtime}}</td>
                        <td>
                          <button @click="open_record_content(record.record.id)" type="button" class="btn btn-success"  >查看录像</button>
                         
                          
                        </td>
                         
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
import router from "../../router/index";
export default{
   components:{
       ContentField,
   },
   setup(){
    const store=useStore();
    let records =ref([]);
   let current_page=1;//定义当前页面
   let total_records=0;//存储当前的对局数量
   let pages=ref([]);//全局变量数组,在里面存储页面跳转的相关信息

   console.log(total_records);//定义的变量一定要使用否则会报ce


   /*//update_pages函数用来更新
   const update_pages=()=>{
      
       //计算一共有多少pages
       let max_pages=parseInt(Math.ceil(total_records/20));
       let new_pages=[];//数组，里面存有所有的分页栏页码
       console.log(max_pages);
       //枚举前后两页，如果有这一页则在分页栏中展示这一页
       for(let i=current_page-2;i<=current_page+2;i++){
        
         console.log(current_page);
    
         if(i>=1&&i<=max_pages){
            new_pages.push({
               number:i,
               is_active: i === current_page ? "active" : "",
         });
         }
       }
       pages.value=new_pages;
       console.log(new_pages.number);
       console.log(new_pages.is_active);
       console.log(pages.value.number);
       console.log(pages.value.is_active);
   }*/

   //辅助函数用来当点击对应的页码跳转到对应的页面

   const click_page =page =>{
      if(page === -2) page =current_page-1;
      else if(page === -1) page =current_page+1;
      let max_pages=parseInt(Math.ceil(total_records/20));

      //如果范围合法，定义一个新的分页
        if(page>=1&&page<=max_pages){
            pull_page(page);
        }
   }

   const update_pages = () =>{
      let max_pages=parseInt(Math.ceil(total_records/20));
      let new_pages =[];
      console.log(max_pages);

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
             url:"http://127.0.0.1:3000/record/getlist/",
             type:"GET",
             data:{
                page,
             },
             headers:{
                Authorization:"Bearer "+store.state.user.token,
             },
             success(resp){
                console.log(resp);
                records.value=resp.records;
                total_records=resp.record_count;
                //返回完信息之后都需要更新页面以更新页码
                update_pages();
             },
             error(resp){
                console.log(resp);
             }
        })
    }

    pull_page(current_page);
    
    //定义函数用来将一维数组转化成二维数组
    const stringTo2D=map=>{
      let g =[];
      //k用来枚举一维数组
      for(let i=0,k=0;i<13;i++){
         let line =[];
         for(let j=0;j<14;j++,k++){
            if(map[k]==='0')line.push(0);
            else line.push(1);
         }
         g.push(line);
         
      }
      return g;
    }
    
    //定义函数跳转到一个新的页面
    const open_record_content =recordId =>{

      //在函数中将某一个record找出来
      for(const record of records.value){
         if(record.record.id===recordId){
            store.commit("updateIsRecord",true);
            //跳转页面之前首先先存储下信息
            store.commit("updateGame",{
               map:stringTo2D(record.record.map),
               a_id:record.record.aid,
               a_sx:record.record.asx,
               a_sy:record.record.asy,
               b_id:record.record.bid,
               b_sx:record.record.bsx,
               b_sy:record.record.bsy,
               
            });
            //存储下每一步的操作信息，以方便回放
            store.commit("updateSteps",{
               a_steps:record.record.asteps,
               b_steps:record.record.bsteps,
            });
            //存储下最终对决的输赢信息
            store.commit("updateRecordLoser",record.record.loser),
            //跳转到某个页面
            router.push({
               name:"record_content",
               params:{
                  recordId:recordId,
               }
            })
            break;//找到想要的record就直接break
         }
      }
    } 
    
    return{
        records,
        open_record_content,
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