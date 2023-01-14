import $ from 'jquery'


export default {

    state: {
        AcWingOS:"AcWingOS",//初始无值，先随意定义字符串
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
        pulling_info: true,//表示当前是否在获取信息当中

    },
    getters: {
    },
    //修改数据
    mutations: {
        updateUser(state, user) {
            //将user信息复制到state中
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;

            state.is_login = user.is_login;
        },
        //辅助函数用来取出token
        updateToken(state, token) {
            state.token = token;
        },
        logout(state) {
            state.id = "",
                state.username = "",
                state.photo = "",
                state.token = "",
                state.is_login = false;
        },
        //定义函数用来更新token
        
        updatePullingInfo(state, pulling_info) {
            state.pulling_info = pulling_info;
        }

    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "https://app2753.acapp.acwing.com.cn/api/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    //当登录成功则返回保存全局信息
                    //当返回的error_message为success时，返回token
                    if (resp.error_message === "success") {
                        localStorage.setItem("jwt_token", resp.token);//将token存在硬盘的一小块空间中
                        context.commit("updateToken", resp.token);
                        //成功执行回调函数:
                        data.success(resp);
                    } else {
                        //失败调用回调函数
                        data.error(resp);
                    }
                },
                error(resp) {
                    //console.log(resp);
                    //登录失败执行回调函数
                    data.error(resp);
                }
            });
        },
        getinfo(context, data) {
            $.ajax({
                url: "https://app2753.acapp.acwing.com.cn/api/user/account/info/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        context.commit("updateUser", {
                            //解析出resp的内容
                            ...resp,
                            is_login: true,
                        })
                        //调用回调函数:
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }

                },
                error(resp) {
                  //console.log(resp);
                    data.error(resp)
                }


            });
        },
        logout(context) {
            localStorage.removeItem("jwt_token");//将存储的登录的token释放掉
            context.commit("logout");
        },

    },
    modules: {
    }

}