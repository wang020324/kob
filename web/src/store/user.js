import $ from 'jquery'

export default {

    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
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
            state.token = user.token;
            state.is_login = user.is_login;
        },
        //辅助函数用来更新token
        updateToken(state, token) {
            state.token = token;
        }
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/token/",
                type: "get",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    //当登录成功则返回保存全局信息
                    //当返回的error_message为success时，返回token
                    if (resp.error_message === "success") {
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
            })
        }
    },
    modules: {
    }

}