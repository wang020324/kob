<template>
    <ContentField>
      <div class="game-table">
         <div>
            <table >
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
                        <td class="game-table-username">
                           <img :src="record.a_photo" alt="" class="record-user-photo">
                           &nbsp;
                           <span class="record-user-username">{{ record.a_username }}</span>
                        </td>
                        <td class="game-table-username">
                           <img :src="record.b_photo" alt="" class="record-user-photo">
                           <span class="record-user-username">{{ record.b_username }}</span>
                        </td>
                        <td>{{ record.result }}</td>
                        <td>{{ record.record.createtime}}</td>
                        <td>
                          <button @click="open_record_content(record.record.id)" type="button"   >查看录像</button>
                         
                          
                        </td>
                         
                      </tr>
                    </tbody>
                  </table>
                  <nav aria-label="Page navigation example">
                    <ul  style="padding:0;">
                      <li class="game-page-item">
                        <a class="game-page-link" href="#" @click="click_page(-2)">&laquo;</a>
                     </li>
                     <li :class="'game-page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                        <a class="game-page-link" href="#">{{ page.number }}</a>
                     </li>
                  
                      <li class="game-page-item">
                        <a class="game-page-link" href="#" @click="click_page(-1)">&raquo;</a>
                     </li>
                   </ul>
             </nav>
         </div>
      </div>
     
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
    let records =ref([]);
   let current_page=1;//定义当前页面
   let total_records=0;//存储当前的对局数量
   let pages=ref([]);//全局变量数组,在里面存储页面跳转的相关信息

   //console.log(total_records);//定义的变量一定要使用否则会报ce



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
             url:"https://app2753.acapp.acwing.com.cn/api/record/getlist/",
             type:"GET",
             data:{
                page,
             },
             headers:{
                Authorization:"Bearer "+store.state.user.token,
             },
             success(resp){
               // console.log(resp);
                records.value=resp.records;
                total_records=resp.record_count;
                //返回完信息之后都需要更新页面以更新页码
                update_pages();
             },
             error(){
                //console.log(resp);
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
         if(record.record.id === recordId){
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
            store.commit("updateRecordLoser",record.record.loser);
            //实现跳转到录像页面
            store.commit("updateRouterName","record_content");
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
div.game-table{
   display: flex;
   justify-content: center;
   align-items:center;
   width:100%;
   height:100%;
}
.game-table-username{
   text-align:left;
   overflow:hidden;
   text-overflow:ellipsis;
   white-space: nowrap;
   max-width:7.5vw;
}
div.game-table table{
   background-color:rgba(255,255,255,0.5);
   border-radius:5px;
}
td{
   width:7.5vw;
}
th{
   text-align: center ;
}
.game-page-item{
    display:inline-block;
    padding:8px 12px;
   background-color:white;
   border:1px solid #dee2e6;
   user-select:none;
}
.game-page-item:hover{
   background-color:#E9ECEF;
   cursor:pointer;

}
.game-page-item.active{
   background-color:#0d6efd;
}

.game-page-item.active >a{
    color:white;
}

.game-page-link{
   color:#0d6efd;
   text-decoration: none;
   
}
nav{
   display:flex;
   justify-content: center;
   align-items:center;
}
</style>