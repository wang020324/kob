export default {
    state: {
        is_record: false,//判断当前组件是否是录像，默认为false
        a_steps: "",//第一名玩家的走法记录
        b_steps: "",//第二名玩家的走法记录
        record_loser: "",//用来标注输赢


    },
    getters: {

    },
    mutations: {
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateSteps(state, data) {
            state.a_steps = data.a_steps,
            state.b_steps = data.b_steps;
        },
        updateRecordLoser(state, loser) {
            state.record_loser = loser;
        }

    },
    actions: {
    },
    modules: {

    }
}