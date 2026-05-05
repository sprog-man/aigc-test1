<template>
  <el-footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3>AI学习交流平台</h3>
        <p>分享AI知识，共同学习成长</p>
      </div>

      <div class="footer-section">
        <h4>快速链接</h4>
        <ul>
          <li><router-link to="/">首页</router-link></li>
          <li><router-link to="/ranking">排行榜</router-link></li>
          <li><router-link to="/posts">帖子列表</router-link></li>
          <li><router-link to="/my/profile">个人中心</router-link></li>
        </ul>
      </div>

      <div class="footer-section">
        <h4>帮助中心</h4>
        <ul>
          <li><a href="#" @click.prevent>使用指南</a></li>
          <li><a href="#" @click.prevent>常见问题</a></li>
          <li><a href="#" @click.prevent>联系我们</a></li>
          <li><a href="#" @click.prevent>隐私政策</a></li>
        </ul>
      </div>

      <div class="footer-section">
        <h4>关注我们</h4>
        <div class="social-links">
          <a href="#" class="social-link">
            <el-icon><Platform /></el-icon>
          </a>
          <a href="#" class="social-link">
            <el-icon><Message /></el-icon>
          </a>
          <a href="#" class="social-link">
            <el-icon><Share /></el-icon>
          </a>
        </div>
      </div>
    </div>

    <div class="footer-bottom">
      <p>&copy; 2024 AI学习交流平台. All rights reserved.</p>
    </div>
  </el-footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
.footer {
  background: #2c3e50;
  color: #ecf0f1;
  padding: 40px 0 20px;
  margin-top: 40px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin-bottom: 30px;
}

.footer-section h3 {
  color: #3498db;
  margin-bottom: 15px;
  font-size: 18px;
}

.footer-section h4 {
  color: #ecf0f1;
  margin-bottom: 15px;
  font-size: 16px;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 8px;
}

.footer-section a {
  color: #bdc3c7;
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-section a:hover {
  color: #3498db;
}

.social-links {
  display: flex;
  gap: 15px;
}

.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: #34495e;
  border-radius: 50%;
  color: #ecf0f1;
  transition: all 0.3s ease;
}

.social-link:hover {
  background: #3498db;
  transform: translateY(-2px);
}

.footer-bottom {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #34495e;
  color: #95a5a6;
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .footer-section h3,
  .footer-section h4 {
    text-align: center;
  }

  .social-links {
    justify-content: center;
  }
}
</style>