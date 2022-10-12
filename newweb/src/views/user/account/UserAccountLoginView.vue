<template>
    <ContentField v-if="!$store.state.user.pulling_info">
          <div class="row justify-content-md-center">
            <div class="col-3">
              <form @submit.prevent="login">
                <div class="mb-3">
                  <label for="username" class="form-label">用户名</label>
                  <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                </div>
                <div class="mb-3">
                  <label for="password" class="form-label">密码</label>
                  <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                </div>
            <div class="error_message">{{ error_message }}</div>
                <button type="submit" class="btn btn-primary ">登录</button>
              </form>
            </div>
          </div>
    </ContentField>
</template>

<script>
import ContentField from "../../../components/ContentField.vue";
import { useStore } from 'vuex';
import { ref } from 'vue'
import router from '../../../router/index'

export default{
   components:{
       ContentField
   },
   setup(){
    //取出全局变量
    const store=useStore();
    //定义三个变量，初始为空
    let username=ref('');
    let password=ref('');
    let error_message=ref('');
    let show_content=false;//初始定为false，防止在刷新的时候出现空白，不雅观
    const jwt_token=localStorage.getItem("jwt_token");//取出token
    //如果token不存在，则返回一个空,存在则存下来
    if(jwt_token){
      store.commit("updateToken",jwt_token);//更新进去新的token去内存
      //验证token是否合法
      store.dispatch("getinfo",{
        success(){
          //成功的话(也就是合法的话)就跳转到首页
          router.push({name:"home"});
          store.commit("updatePullingInfo",false);
        },
        error(){
          //show_content.value=true;
          store.commit("updatePullingInfo",false);
        }

      })

    }else
    {
      //show_content.value=true;
      store.commit("updatePullingInfo",false);
    }

    //定义登录触发函数
    const login = () =>{
      error_message.value="";//初始一定要清空
      store.dispatch("login",{
        username:username.value,
        password:password.value,
        success(resp){
          store.dispatch("getinfo",{
            //如果返回信息成功
                success(){
                  router.push({name:'home'});
                  console.log(store.state.user);//输出查看信息
                }
          });
         
          console.log(resp);
        },
        error(){
          //console.log(resp);
           error_message.value="用户名或密码错误";
        }
      })
    }

    return {
      username,
      password,
      error_message,
      login,
      show_content,
    }
   }
}

</script>

<style scoped>
button{
    width:100%;
}
div.error_message{
    color: red;
}
</style>