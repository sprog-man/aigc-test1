import { createStore } from 'vuex'
import auth from './modules/auth'
import posts from './modules/posts'
import user from './modules/user'
import activities from './modules/activities'

export default createStore({
  modules: {
    auth,
    posts,
    user,
    activities
  }
})