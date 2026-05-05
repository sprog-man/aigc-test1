# AI学习交流网站部署指南

## 环境要求

### 后端环境
- Java 17 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本

### 前端环境
- Node.js 16 或更高版本
- npm 或 yarn

## 部署步骤

### 1. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE ai_learning_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 执行数据库脚本
```bash
cd backend
mysql -u your_username -p ai_learning_platform < src/main/resources/database.sql
```

### 2. 后端配置

#### 修改数据库连接
编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_learning_platform?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
    username: your_mysql_username
    password: your_mysql_password
```

#### 启动后端服务
```bash
cd backend
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

### 3. 前端配置

#### 安装依赖
```bash
cd frontend
npm install
```

#### 启动开发服务器
```bash
npm run serve
```

前端服务将在 http://localhost:3000 启动

## 功能验证

### 1. 用户注册和登录
- 访问 http://localhost:3000
- 点击"注册"按钮创建新用户
- 使用用户名和密码登录

### 2. 发布帖子
- 登录后点击"发布帖子"
- 填写标题和内容
- 设置学习币价格
- 提交帖子

### 3. 获取学习币
- 每日签到：获得 10 学习币
- 浏览帖子：获得 1 学习币
- 发布评论：获得 5 学习币
- 发布帖子：获得 20 学习币
- 点赞帖子：获得 2 学习币

### 4. 购买帖子
- 点击帖子详情页的"购买"按钮
- 使用学习币购买内容
- 购买后可以查看完整内容

### 5. 查看排行榜
- 访问排行榜页面
- 查看学习币排行榜和签到天数排行榜
- 前50名用户会显示，前三名有特殊标识

## API文档

### 认证接口
- POST /api/auth/register - 用户注册
- POST /api/auth/login - 用户登录
- GET /api/auth/me - 获取用户信息

### 帖子接口
- GET /api/posts - 获取帖子列表
- POST /api/posts - 创建帖子
- GET /api/posts/{id} - 获取帖子详情
- PUT /api/posts/{id} - 更新帖子
- DELETE /api/posts/{id} - 删除帖子

### 评论接口
- POST /api/comments - 创建评论
- GET /api/comments/post/{postId} - 获取帖子评论

### 活动接口
- POST /api/activities/daily-sign-in - 每日签到
- GET /api/activities/learning-coin-ranking - 学习币排行榜

## 故障排除

### 1. 数据库连接失败
- 检查MySQL服务是否启动
- 验证数据库用户名和密码
- 确认数据库名称正确

### 2. 后端启动失败
- 检查Java环境
- 确认端口8080未被占用
- 查看控制台错误信息

### 3. 前端启动失败
- 检查Node.js环境
- 删除node_modules文件夹后重新安装依赖
- 检查端口3000是否被占用

## 生产环境部署

### 后端打包
```bash
cd backend
mvn clean package
java -jar target/ai-learning-platform-0.0.1-SNAPSHOT.jar
```

### 前端打包
```bash
cd frontend
npm run build
# 将dist目录部署到Web服务器
```

## Git分支说明

当前项目有以下分支：
- master: 主分支，稳定版本
- claude: 开发分支，用于后续开发和维护

## 联系方式

如有问题，请查看项目文档或提交Issue。