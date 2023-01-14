import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RecordContentView from '../views/record/RecordContentView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import NotFound from '../views/error/NotFound'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'
import UserAccountReceiveCodeView from '../views/user/account/UserAccountReceiveCodeView'
import store from '../store/index'



const routes = [
  //根路径重定向到对战页面
  {
    path: '/',
    name: "home",
    redirect: "/pk/",
    meta: {
      requestAuth: true,
    }

  },
  {
    path: "/pk/",//相对路径
    name: "pk_index",
    component: PkIndexView,//映射的组件
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    }

  },
  {
    path: "/record/:recordId/",
    name: "record_content",
    component: RecordContentView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path:"/user/account/acwing/web/receive_code/",
    name:"user_account_web_receive_code",
    component:UserAccountReceiveCodeView,
    meta:{
      requestAuth:false,
    }
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
    meta: {
      requestAuth: false,
    }
  },

  {
    path: "/:catchAl(.*)",
    redirect: "/404/"
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

//实现前端页面的授权
router.beforeEach((to, from, next) => {
  //如果下一个想要跳转的页面需要登录而且当前没有登录状态的话，那么就需要重定向到登录页面
  if (to.meta.requestAuth && !store.state.user.is_login)
    next({ name: "user_account_login" });
  else
    next();
})

export default router

