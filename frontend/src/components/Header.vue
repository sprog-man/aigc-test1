<template>
  <el-header class="header" height="60px">
    <div class="header-content">
      <div class="header-left">
        <router-link to="/" class="logo">
          <h2>AI学习平台</h2>
        </router-link>
        <nav class="nav-menu">
          <el-menu
            :default-active="$route.path"
            mode="horizontal"
            router
            class="horizontal-menu"
          >
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/ranking">排行榜</el-menu-item>
            <el-menu-item v-if="isAdmin" index="/admin">管理后台</el-menu-item>
          </el-menu>
        </nav>
      </div>

      <div class="header-right">
        <!-- 用户信息 -->
        <div v-if="isAuthenticated" class="user-info">
          <el-dropdown @command="handleCommand">
            <span class="user-profile">
              <el-avatar :src="user?.avatarUrl" :size="32" class="avatar">
                {{ user?.fullName?.charAt(0) || user?.username?.charAt(0) }}
              </el-avatar>
              <span class="user-name">{{ user?.fullName || user?.username }}</span>
              <el-badge :value="balance" class="balance-badge" type="success">
                学习币
              </el-badge>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon> 个人资料
                </el-dropdown-item>
                <el-dropdown-item command="posts">
                  <el-icon><Document /></el-icon> 我的帖子
                </el-dropdown-item>
                <el-dropdown-item command="purchases">
                  <el-icon><ShoppingCart /></el-icon> 我的购买
                </el-dropdown-item>
                <el-dropdown-item command="activities">
                  <el-icon><TrendCharts /></el-icon> 活动记录
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>

        <!-- 登录注册按钮 -->
        <div v-else class="auth-buttons">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button type="primary" @click="$router.push('/register')">注册</el-button>
        </div>

        <!-- 发布按钮 -->
        <el-button
          v-if="isAuthenticated"
          type="primary"
          @click="$router.push('/posts/create')"
          class="publish-btn"
        >
          <el-icon><Plus /></el-icon>
          发布
        </el-button>
      </div>
    </div>
  </el-header>
</template>

<script>
import { computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

export default {
  name: 'Header',
  setup() {
    const store = useStore()
    const router = useRouter()

    const isAuthenticated = computed(() => store.getters['auth/isAuthenticated'])
    const user = computed(() => store.getters['auth/user'])
    const isAdmin = computed(() => store.getters['auth/isAdmin'])
    const balance = computed(() => store.getters['activities/balance'])

    const handleCommand = async (command) => {
      switch (command) {
        case 'profile':
          router.push('/my/profile')
          break
        case 'posts':
          router.push('/my/posts')
          break
        case 'purchases':
          router.push('/my/purchases')
          break
        case 'activities':
          router.push('/activities')
          break
        case 'logout':
          try {
            store.dispatch('auth/logout')
            ElMessage.success('退出成功')
            router.push('/')
          } catch (error) {
            ElMessage.error('退出失败')
          }
          break
      }
    }

    return {
      isAuthenticated,
      user,
      isAdmin,
      balance,
      handleCommand
    }
  }
}
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 30px;
}

.logo h2 {
  margin: 0;
  color: #409eff;
  font-size: 20px;
  cursor: pointer;
}

.horizontal-menu {
  border-bottom: none;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 5px;
}

.user-name {
  font-size: 14px;
  color: #333;
}

.balance-badge {
  cursor: pointer;
}

.avatar {
  border: 2px solid #f0f0f0;
}

.auth-buttons {
  display: flex;
  gap: 10px;
}

.publish-btn {
  margin-left: 20px;
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 10px;
  }

  .header-left {
    gap: 15px;
  }

  .logo h2 {
    font-size: 18px;
  }

  .user-name {
    display: none;
  }

  .publish-btn {
    margin-left: 10px;
  }
}
</style>