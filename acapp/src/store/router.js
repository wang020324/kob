export default {
    state: {
       router_name:"menu"//表示当前应该在哪个页面下，默认在菜单页面 menu,pk,record,record_content,ranklist,user_bot
       
   
    },
    getters: {

    },
    mutations: {
        updateRouterName(state,router_name){
            state.router_name=router_name;
        }

    },
    actions: {
    },
    modules: {

    }
}