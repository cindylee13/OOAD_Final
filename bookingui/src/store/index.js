import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userInfo: { signedIn: false }
  },
  mutations: {
    userInfoChange(state, user){
      state.userInfo.signedIn = user;
    }
  },
  actions: {
  },
  modules: {
  }
})
