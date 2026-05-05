import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import PostList from '../views/PostList.vue'
import PostDetail from '../views/PostDetail.vue'
import CreatePost from '../views/CreatePost.vue'
import MyPosts from '../views/MyPosts.vue'
import MyProfile from '../views/MyProfile.vue'
import Ranking from '../views/Ranking.vue'
import Admin from '../views/Admin.vue'
import NotFound from '../views/NotFound.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true }
  },
  {
    path: '/posts',
    name: 'PostList',
    component: PostList
  },
  {
    path: '/posts/:id',
    name: 'PostDetail',
    component: PostDetail
  },
  {
    path: '/posts/create',
    name: 'CreatePost',
    component: CreatePost,
    meta: { requiresAuth: true }
  },
  {
    path: '/my/posts',
    name: 'MyPosts',
    component: MyPosts,
    meta: { requiresAuth: true }
  },
  {
    path: '/my/profile',
    name: 'MyProfile',
    component: MyProfile,
    meta: { requiresAuth: true }
  },
  {
    path: '/ranking',
    name: 'Ranking',
    component: Ranking
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 检查是否需要认证
  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }

  // 检查是否需要管理员权限
  if (to.meta.requiresAdmin) {
    const user = JSON.parse(localStorage.getItem('user'))
    if (user && user.roles && user.roles.includes('ADMIN')) {
      next()
    } else {
      next('/dashboard')
    }
    return
  }

  next()
})

export default router