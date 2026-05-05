<template>
  <div class="home">
    <Header />

    <div class="main-content">
      <!-- Hero Section -->
      <div class="hero-section">
        <div class="hero-content">
          <h1>AI学习交流平台</h1>
          <p>分享AI知识，获取学习币，提升学习效果</p>
          <div class="hero-buttons">
            <el-button type="primary" size="large" @click="$router.push('/posts/create')">
              <el-icon><Plus /></el-icon>
              开始分享
            </el-button>
            <el-button size="large" @click="$router.push('/ranking')">
              <el-icon><Trophy /></el-icon>
              查看排行榜
            </el-button>
          </div>
        </div>
      </div>

      <!-- 功能特点 -->
      <div class="features-section">
        <h2 class="section-title">平台特色</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><Coin /></el-icon>
            </div>
            <h3>学习币系统</h3>
            <p>1人民币=10学习币，通过签到、浏览、评论等行为获取学习币</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><Document /></el-icon>
            </div>
            <h3>知识分享</h3>
            <p>分享AI知识，设置价格，通过优质内容获得收益</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><Trophy /></el-icon>
            </div>
            <h3>活跃排行榜</h3>
            <p>学习活跃度排名，前50名获得特殊展示，前三名有特殊标识</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><UserFilled /></el-icon>
            </div>
            <h3>用户系统</h3>
            <p>完整的用户注册、登录、个人资料管理功能</p>
          </div>
        </div>
      </div>

      <!-- 热门帖子 -->
      <div class="posts-section">
        <div class="section-header">
          <h2 class="section-title">热门帖子</h2>
          <el-button type="primary" @click="$router.push('/posts')">
            查看全部 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>

        <div v-if="loading" class="loading">
          <el-skeleton :rows="3" animated />
        </div>

        <div v-else-if="popularPosts.length > 0" class="posts-grid">
          <div v-for="post in popularPosts" :key="post.id" class="post-card">
            <div class="post-header">
              <el-avatar :src="post.authorName" :size="40" class="author-avatar">
                {{ post.authorName?.charAt(0) }}
              </el-avatar>
              <div class="post-info">
                <div class="author-name">{{ post.authorName }}</div>
                <div class="post-time">{{ formatDate(post.createdAt) }}</div>
              </div>
            </div>
            <h3 class="post-title">{{ post.title }}</h3>
            <p class="post-content">{{ truncateContent(post.content) }}</p>
            <div class="post-stats">
              <span class="stat-item">
                <el-icon><View /></el-icon>
                {{ post.viewCount }}
              </span>
              <span class="stat-item">
                <el-icon><Star /></el-icon>
                {{ post.likeCount }}
              </span>
              <span class="stat-item">
                <el-icon><ShoppingCart /></el-icon>
                {{ post.purchaseCount }}
              </span>
            </div>
            <div class="post-footer">
              <div class="post-price">
                <el-icon><Coin /></el-icon>
                {{ formatPrice(post.price) }}
              </div>
              <el-button
                type="primary"
                size="small"
                @click="$router.push(`/posts/${post.id}`)"
              >
                查看详情
              </el-button>
            </div>
          </div>
        </div>

        <div v-else class="no-posts">
          <el-empty description="暂无热门帖子">
            <el-button type="primary" @click="$router.push('/posts')">
              去发帖
            </el-button>
          </el-empty>
        </div>
      </div>

      <!-- 排行榜预览 -->
      <div class="ranking-section">
        <div class="section-header">
          <h2 class="section-title">学习活跃排行榜</h2>
          <el-button @click="$router.push('/ranking')">
            查看完整排名 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>

        <div v-if="ranking.length > 0" class="ranking-list">
          <div v-for="(user, index) in ranking.slice(0, 3)" :key="user.userId" class="ranking-item">
            <div class="rank-number" :class="{ 'top-three': index < 3 }">
              {{ index + 1 }}
            </div>
            <div class="user-info">
              <el-avatar :src="user.avatarUrl" :size="32" class="user-avatar">
                {{ user.fullName?.charAt(0) }}
              </el-avatar>
              <div class="user-details">
                <div class="user-name">{{ user.fullName || user.username }}</div>
                <div class="user-stats">
                  <span class="stat">学习币: {{ user.learningCoins }}</span>
                  <span class="stat">签到: {{ user.totalSignDays }}天</span>
                </div>
              </div>
            </div>
            <div class="rank-icon" v-if="index < 3">
              {{ index === 0 ? '🥇' : index === 1 ? '🥈' : '🥉' }}
            </div>
          </div>
        </div>

        <div v-else class="no-ranking">
          <el-empty description="暂无排名数据">
            <el-button type="primary" @click="$router.push('/register')">
              注册后查看排名
            </el-button>
          </el-empty>
        </div>
      </div>

      <Footer />
    </div>
  </div>
</template>

<script>
import { computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'

export default {
  name: 'Home',
  components: {
    Header,
    Footer
  },
  setup() {
    const store = useStore()

    const loading = computed(() => store.getters['posts/loading'])
    const popularPosts = computed(() => store.getters['posts/posts'])
    const ranking = computed(() => store.getters['activities/learningRanking'])

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN')
    }

    const truncateContent = (content, length = 100) => {
      if (!content) return ''
      return content.length > length ? content.substring(0, length) + '...' : content
    }

    const formatPrice = (price) => {
      if (!price) return '免费'
      return `${price} 学习币`
    }

    const loadPopularPosts = async () => {
      try {
        await store.dispatch('posts/getPopularPosts', { page: 0, size: 6 })
      } catch (error) {
        ElMessage.error('获取热门帖子失败')
      }
    }

    const loadRanking = async () => {
      try {
        await store.dispatch('activities/getLearningCoinRanking', 10)
      } catch (error) {
        ElMessage.error('获取排行榜失败')
      }
    }

    onMounted(async () => {
      await loadPopularPosts()
      await loadRanking()
    })

    return {
      loading,
      popularPosts,
      ranking,
      formatDate,
      truncateContent,
      formatPrice
    }
  }
}
</script>

<style scoped>
.home {
  min-height: 100vh;
}

.main-content {
  padding-top: 80px;
}

.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 80px 20px;
  text-align: center;
}

.hero-content h1 {
  font-size: 3rem;
  margin-bottom: 20px;
  font-weight: 700;
}

.hero-content p {
  font-size: 1.2rem;
  margin-bottom: 40px;
  opacity: 0.9;
}

.hero-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.features-section {
  padding: 80px 20px;
  background: #f8f9fa;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 50px;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  background: white;
  padding: 40px 30px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 3rem;
  color: #409eff;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: #333;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}

.posts-section {
  padding: 80px 20px;
  background: white;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
}

.posts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.post-card {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 25px;
  transition: box-shadow 0.3s ease;
}

.post-card:hover {
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.author-avatar {
  margin-right: 15px;
}

.post-info {
  flex: 1;
}

.author-name {
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.post-time {
  font-size: 12px;
  color: #999;
}

.post-title {
  font-size: 1.2rem;
  margin-bottom: 15px;
  color: #333;
  line-height: 1.4;
}

.post-content {
  color: #666;
  margin-bottom: 20px;
  line-height: 1.6;
}

.post-stats {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  color: #666;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-price {
  display: flex;
  align-items: center;
  gap: 5px;
  font-weight: 600;
  color: #409eff;
}

.no-posts,
.no-ranking {
  text-align: center;
  padding: 60px 20px;
}

.ranking-section {
  padding: 80px 20px;
  background: #f8f9fa;
}

.ranking-list {
  max-width: 800px;
  margin: 0 auto;
}

.ranking-item {
  background: white;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.rank-number {
  font-size: 1.5rem;
  font-weight: 700;
  width: 40px;
  text-align: center;
  color: #666;
}

.rank-number.top-three {
  color: #ffd700;
  font-size: 2rem;
}

.user-info {
  display: flex;
  align-items: center;
  flex: 1;
  margin: 0 20px;
}

.user-avatar {
  margin-right: 15px;
}

.user-details {
  flex: 1;
}

.user-name {
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.user-stats {
  display: flex;
  gap: 15px;
  font-size: 14px;
  color: #666;
}

.rank-icon {
  font-size: 1.5rem;
}

@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2rem;
  }

  .section-title {
    font-size: 1.8rem;
  }

  .hero-buttons {
    flex-direction: column;
    align-items: center;
  }

  .section-header {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }

  .posts-grid {
    grid-template-columns: 1fr;
  }

  .ranking-item {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }

  .user-info {
    margin: 0;
  }
}
</style>