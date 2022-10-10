import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import NotFound from '../views/error/NotFound'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'



const routes = [
  //根路径重定向到对战页面
  {
    path: '/',
    name: "home",
    redirect: "/pk/",

  },
  {
    path: "/pk/",//相对路径
    name: "pk_index",
    component: PkIndexView,//映射的组件
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,

  },
  {
    path: "/ranklist",
    name: "ranklist_index",
    component: RanklistIndexView,

  },
  {
    path: "/user/",
    name: "user_bot_index",
    component: UserBotIndexView,

  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
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

export default router

