<template>
    <ContentField>
         <div class="game-table">
          <div>
            <div>
              <span style="font-size:130%" >My Bot</span>
              <button type="button"  style="float:right;" @click="show_add_modal_handler(true)">
                         创建Bot
              </button>
                </div>
            
                  <!-- Modal -->
                <div class="game-modal" id="add-bot-btn" tabindex="-1"  v-if="show_add_modal" >

                  <div>
                    <h5 style="margin:2px;">创建Bot</h5>
                  </div>
                <div>
                  <div>
                    <label for="add-bot-title" >名称</label>
                    <input style="width:85%" v-model="botadd.title" type="text" id="add-bot-title" placeholder="请输入Bot名称">
                  </div>
                <div>
                    <label for="add-bot-description" >简介</label>
                    <textarea style="width:85%; margin-top:10px" v-model="botadd.description"  id="add-bot-description" rows="3" placeholder="请输入Bot的简介"></textarea>
                  </div>
                  <div>
                  <label for="add-bot-code" >代码</label>
                    <!--<textarea  v-model="botadd.content" class="form-control" id="add-bot-code" rows="7" placeholder="请输入Bot的代码"></textarea>
                    -->
                    <VAceEditor
                    v-model:value="botadd.content"
                    @init="editorInit"
                    lang="c_cpp"
                    theme="textmate"
                    style="height: 300px" />                
                    </div>
              </div>
            <div >
              <div class="error-message">{{ botadd.error_message }}</div>
              <button type="button" @click="show_add_modal_handler(false)">退出</button>
                <button type="button" @click="add_bot">创建</button>
              </div>
            </div>
                <table>
                  <thead>
                    <tr>
                      <th>名称</th>
                      <th>创建时间</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="bot in bots" :key="bot.id">
                      <td>{{ bot.title }}</td>
                      <td>{{ bot.createtime }}</td>
                      <td>
                        <button type="button"  style="margin-right:20px;"  @click="show_update_modal_handler(bot.id,true)">修改</button>
                        <button type="button"  @click="remove_bot(bot)">删除</button>
                        <div class="game-modal" :id="'update-bot-modal-' +bot.id" tabindex="-1" v-if="bot.show_update_modal">
                              <div >
                                <h5 style="margin:2px;">修改Bot</h5>
                             
                              </div>
                            <div >
                          <div >
                            <label for="add-bot-title">名称</label>
                            <input style="width:85%" v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                          </div>
                        <div >
                          <label for="add-bot-description" >简介</label>
                          <textarea style="width:85% margin-top:10px" v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot的简介"></textarea>
                        </div>
                        <!--<br>-->
                        <div>
                          <label for="add-bot-code">代码</label>
                        <!--<textarea  v-model="bot.content" class="form-control" id="add-bot-code" rows="7" placeholder="请输入Bot的代码"></textarea>
                        -->
                        <VAceEditor
                            v-model:value="bot.content"
                            @init="editorInit"
                            lang="c_cpp"
                                theme="textmate"
                                style="height: 300px" />
                            </div>
                        </div>
                            <div >
                              <div class="error-message">{{bot.error_message}}</div>
                              <button type="button" @click="show_update_modal_handler(bot.id,false)">退出</button>
                              <button type="button" @click="update_bot(bot)">保存修改</button>
                            </div>
                          </div>

                      

                      </td>
                        
                    </tr>
                  </tbody>
                </table>
              </div>

          </div>


    </ContentField>
</template>

<script>
import ContentField from "../../../components/ContentField.vue";
//import $ from 'jquery';
//import { useStore } from 'vuex';

import $ from 'jquery';
import { useStore } from 'vuex';
import { ref ,reactive} from 'vue';

import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';


export default{
  components:{
    ContentField,
     VAceEditor
  },
  
    setup(){
      const store=useStore();
      let show_add_modal =ref(false);//定义全局变量，以表示是否需要展示模态框
      ace.config.set(
    "basePath", 
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
  
      //const jwt_token=localStorage.getItem("jwt_token");
      let bots=ref([]);

      const botadd=reactive({
        title:"",
        description:"",
        content:"",
        errpr_message:"",

      })
      //获取列表函数
      const refresh_bots=()=>{
        $.ajax({
          url:"https://app2753.acapp.acwing.com.cn/api/user/bot/getlist/",
          type:"GET",
          
          headers:{
            Authorization:"Bearer "+store.state.user.token,
          },
          success(resp){
            //每次拉取时，都给bot添加属性以判断是否展示修改的模态框
            for(const bot of resp){
              bot.show_update_modal=false;
            }
            bots.value=resp;
          },
           
        })
      }
        refresh_bots();

        //向后端发送请求

        const add_bot=()=>{
              $.ajax({
                url:"https://app2753.acapp.acwing.com.cn/api/user/bot/add/",
                type:"POST",
                data:{
                  title:botadd.title,
                  description:botadd.description,
                  content:botadd.content,
                },
                headers:{
                  Authorization:"Bearer "+store.state.user.token,
                },
                success(resp){
                  if(resp.error_message==="success"){
                    botadd.title="",
                    botadd.description="",
                    botadd.content="",
                    //关闭模态框
                    //Modal.getInstance("#add-bot-btn").hide();
                    show_add_modal.value=false;
                    refresh_bots();
                  }
                  else{
                    botadd.error_message=resp.error_message;
                  }
                }
              })
        }

        //实现删除按钮'
        const remove_bot=(bot)=>{
          $.ajax({
            url:"https://app2753.acapp.acwing.com.cn/api/user/bot/remove/",
            type:"POST",
            data:{
              bot_id:bot.id,
            },
            headers:{
              Authorization:"Bearer "+store.state.user.token,
            },
            success(resp){
              //console.log(resp);
              //console.log(bot.id);
              //成功删除之后重新刷新整个列表
              if(resp.error_message==="success")
               refresh_bots();
            },
            error(){
              //console.log(resp);
            }
          })


        }

      //实现修改按钮
      const update_bot=(bot)=>{
        bot.error_message="";//初始清空
        $.ajax({
          url:"https://app2753.acapp.acwing.com.cn/api/user/bot/update/",
          type:"POST",
          data:{
            bot_id:bot.id,
            title:bot.title,
            description:bot.description,
            content:bot.content,
          },
          headers:{
            Authorization:"Bearer "+store.state.user.token,
          },
          success(resp){
            if(resp.error_message==="success"){
             // console.log(resp);
           
            //  Modal.getInstance('#update-bot-modal-' +bot.id).hide()
              refresh_bots();
            }else{
              bot.error_message=resp.error_message;
            }
          },
          
        })
      }

      //处理函数，以用来显示模态框
      const show_add_modal_handler=is_show=>{
        show_add_modal.value=is_show;
      }
      //处理函数，以用来处理关闭每一个bot的修改模态框的关闭
      const show_update_modal_handler=(bot_id,is_show)=>{
         //首先定义一个新的数组
         const new_bots=[];
         //循环枚举之前的每个数组
         for(const bot of bots.value){
          if(bot.id===bot_id){
            bot.show_update_modal=is_show;
          }
          new_bots.push(bot);
         }
         bots.value=new_bots;
      }

        return{
          bots,
          add_bot,
          remove_bot,
          update_bot,
          botadd,
          show_add_modal,
          show_add_modal_handler,
          show_update_modal_handler,
        }
      }

}

</script>

<style scoped>
div.error-message{
  color:red;
}
div.game-table{
   display: flex;
   justify-content: center;
   padding-top:5vh;
   width:100%;
   height:calc(100%-5vh);
}

div.game-table table{
   background-color:rgba(255,255,255,0.5);
   border-radius:5px;
}
td{
   overflow:hidden;
   text-overflow:ellipsis;
   white-space:nowrap;
   width: 15vw;
   max-width:12vw;
   text-align:center;
}
th{
   text-align: center;
}
.game-modal{
  background-color:white;
  padding:10px;
  border-radius: 5px;
  position:absolute;
  width:40vw;
  height:57vh;
  left:0;
  right:0;
  top:0;
  bottom:0;
  margin:auto;
  text-align:left;

}

</style>