<template>
    <ContentField>
        <div class="container">
          <div class="row">
            <div class="col-3">
              <div class="card" style="margin-top: 20px;">
                <div class="card-body">
              <img :src="$store.state.user.photo" alt="" style="width:100%;">
               </div>
              </div>
            </div>
          <div class="col-9">
            <div class="card" style="margin-top:20px">
              <div class="card-header ">
                     <span class="header">My Bot</span>
                   <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                    创建Bot
                   </button>

                   <!-- Modal -->
                 <div class="modal fade" id="add-bot-btn" tabindex="-1" >
                  <div class="modal-dialog modal-xl">
                   <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" >创建Bot</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                  <div class="modal-body">
                    <div class="mb-3">
                     <label for="add-bot-title" class="form-label">名称</label>
                     <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                   </div>
                   <div class="mb-3">
                     <label for="add-bot-description" class="form-label">Bot的简介</label>
                     <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot的简介"></textarea>
                   </div>
                   <div>
                   <label for="add-bot-code" class="form-label">Bot的代码</label>
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
              <div class="modal-footer">
                <div class="error-message">{{botadd.error_message}}</div>
               <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">退出</button>
                 <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
              </div>
             </div>
              </div>
             </div>
              </div>
              <div class="card-body">
                  <table class="table table-striped table-hover">
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
                          <button type="button" class="btn btn-success" style="margin-right:20px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-' +bot.id">修改</button>
                          <button type="button" class="btn btn-danger" @click="remove_bot(bot)">删除</button>
                          <div class="modal fade" :id="'update-bot-modal-' +bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                              <div class="modal-content">
                                <div class="modal-header">
                                 <h5 class="modal-title" >创建Bot</h5>
                                 <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                               </div>
                              <div class="modal-body">
                                <div class="mb-3">
                                 <label for="add-bot-title" class="form-label">名称</label>
                                 <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                               </div>
                              <div class="mb-3">
                               <label for="add-bot-description" class="form-label">Bot的简介</label>
                               <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot的简介"></textarea>
                              </div>
                            <div>
                              <label for="add-bot-code" class="form-label">Bot的代码</label>
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
                              <div class="modal-footer">
                                <div class="error-message">{{bot.error_message}}</div>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">退出</button>
                                <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存修改</button>
                              </div>
                            </div>
                             </div>
                            </div>
                        
                        
                        </td>
                         
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              </div>
            </div>
          </div>
    </ContentField>
</template>

<script>
//import ContentField from "../../../components/ContentField.vue";
//import $ from 'jquery';
//import { useStore } from 'vuex';

import $ from 'jquery';
//import { useStore } from 'vuex';
import { ref ,reactive} from 'vue';
import { Modal } from 'bootstrap/dist/js/bootstrap';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';


export default{
  components:{
     VAceEditor
  },
  
    setup(){
      ace.config.set(
    "basePath", 
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
  
      const jwt_token=localStorage.getItem("jwt_token");
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
          url:"http://127.0.0.1:3000/user/bot/getlist/",
          type:"GET",
          
          headers:{
            Authorization:"Bearer "+jwt_token,
          },
          success(resp){
            bots.value=resp;
          },
           
        })
      }
        refresh_bots();

        //向后端发送请求

        const add_bot=()=>{
              $.ajax({
                url:"http://127.0.0.1:3000/user/bot/add/",
                type:"POST",
                data:{
                  title:botadd.title,
                  description:botadd.description,
                  content:botadd.content,
                },
                headers:{
                  Authorization:"Bearer "+jwt_token,
                },
                success(resp){
                  if(resp.error_message==="success"){
                    botadd.title="",
                    botadd.description="",
                    botadd.content="",
                    //关闭模态框
                    Modal.getInstance("#add-bot-btn").hide();
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
            url:"http://127.0.0.1:3000/user/bot/remove/",
            type:"POST",
            data:{
              bot_id:bot.id,
            },
            headers:{
              Authorization:"Bearer "+jwt_token,
            },
            success(resp){
              console.log(resp);
              console.log(bot.id);
              //成功删除之后重新刷新整个列表
              if(resp.error_message==="success")
               refresh_bots();
            },
            error(resp){
              console.log(resp);
            }
          })


        }

      //实现修改按钮
      const update_bot=(bot)=>{
        bot.error_message="";//初始清空
        $.ajax({
          url:"http://127.0.0.1:3000/user/bot/update/",
          type:"POST",
          data:{
            bot_id:bot.id,
            title:bot.title,
            description:bot.description,
            content:bot.content,
          },
          headers:{
            Authorization:"Bearer "+jwt_token,
          },
          success(resp){
            if(resp.error_message==="success"){
             // console.log(resp);
           
              Modal.getInstance('#update-bot-modal-' +bot.id).hide();
              refresh_bots();
            }else{
              bot.error_message=resp.error_message;
            }
          },
          
        })
      }

        return{
          bots,
          add_bot,
          remove_bot,
          update_bot,
          botadd,
        }
      }

}

</script>

<style scoped>
.header{
  font-size:120%;
  font-weight:200px;
}
div.error-message{
  color:red
}
</style>