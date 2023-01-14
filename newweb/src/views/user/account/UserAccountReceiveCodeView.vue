

<script>
import router  from '@/router/index'
import { useRoute } from 'vue-router';
import $ from 'jquery';
import { useStore } from 'vuex';


export default{
    setup(){
        const myRoute=useRoute();
        const store=useStore();

        $.ajax({
            url:"https://app2753.acapp.acwing.com.cn/api/user/account/acwing/web/receive_code/",
            type:"GET",
            data:{
                code:myRoute.query.code,
                state:myRoute.query.state,
            },
            success:resp=>{
                if(resp.result==="success"){
                    //将token存到内存当中
                    localStorage.setItem("jwt_token",resp.jwt_token);
                    store.commit("updateToken",resp.jwt_token);
                    //调用success，成功跳转到home
                    router.push({name:"home"});
                    store.commit("updatePullingInfo",false);
                }else{
                    //失败跳转到登录页面
                    router.push({name:"user_account_login"});

                }
            }

        })
        
    }
}
</script>



<style scoped>

</style>