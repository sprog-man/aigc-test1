# AI学习交流网站

一个基于Spring Boot + Vue.js的AI知识分享平台，支持用户发布付费帖子、使用学习币购买内容、获取学习币积分等功能。

## 项目结构

```
MyAgentTeam2/
├── backend/                 # Spring Boot 后端
│   ├── src/main/java/com/aiteam/
│   │   ├── controller/     # 控制器层
│   │   ├── service/        # 业务逻辑层
│   │   ├── repository/     # 数据访问层
│   │   ├── entity/         # 实体类
│   │   ├── dto/           # 数据传输对象
│   │   ├── config/        # 配置类
│   │   └── util/          # 工具类
│   └── src/main/resources/
│       ├── application.yml # 配置文件
│       └── static/         # 静态资源
└── frontend/                # Vue.js 前端
    ├── src/
    │   ├── api/           # API 接口
    │   ├── assets/        # 静态资源
    │   ├── components/    # 组件
    │   ├── store/         # Vuex 状态管理
    │   ├── views/         # 页面组件
    │   └── router/        # 路由配置
    └── public/            # 公共资源
```

## 主要功能

### 1. 用户系统
- 用户注册和登录
- 个人资料管理
- JWT 身份认证

### 2. 发帖功能
- 发布AI知识帖子
- 设置帖子价格（学习币）
- 帖子审核机制
- 编辑和删除帖子

### 3. 购买功能
- 使用学习币购买帖子
- 购买记录管理
- 收益分配机制

### 4. 学习币系统
- 1人民币 = 10学习币
- 学习币获取方式：
  - 每日签到（10学习币）
  - 浏览帖子（1学习币）
  - 发布评论（5学习币）
  - 发布帖子（20学习币）
  - 点赞帖子（2学习币）

### 5. 排行榜系统
- 学习币排行榜
- 签到天数排行榜
- 前50名展示，前三名特殊标识

## 技术栈

### 后端
- Spring Boot 3.2.0
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.0
- Maven

### 前端
- Vue 3
- Vue Router 4
- Vuex 4
- Element Plus
- Axios

## 环境要求

- Java 17+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+

## 安装和运行

### 后端启动

1. 配置数据库连接
```bash
cd backend
# 修改 application.yml 中的数据库配置
```

2. 初始化数据库
```bash
# 创建数据库
CREATE DATABASE ai_learning_platform;

# 运行项目（会自动创建表）
mvn spring-boot:run
```

3. 后端服务将在 http://localhost:8080 启动

### 前端启动

1. 安装依赖
```bash
cd frontend
npm install
```

2. 启动开发服务器
```bash
npm run serve
```

3. 前端服务将在 http://localhost:3000 启动

## 数据库表结构

### 用户表 (users)
- id: 用户ID
- username: 用户名
- password: 密码（加密）
- email: 邮箱
- full_name: 全名
- learning_coins: 学习币余额
- total_sign_days: 总签到天数
- last_sign_date: 最后签到日期
- created_at: 创建时间

### 帖子表 (posts)
- id: 帖子ID
- title: 标题
- content: 内容
- price: 价格（学习币）
- view_count: 浏览量
- like_count: 点赞数
- purchase_count: 购买数
- is_pinned: 是否置顶
- is_approved: 是否通过审核
- author_id: 作者ID
- created_at: 创建时间

### 评论表 (comments)
- id: 评论ID
- content: 评论内容
- post_id: 帖子ID
- author_id: 作者ID
- parent_id: 父评论ID
- created_at: 创建时间

### 购买记录表 (post_purchases)
- id: 购买ID
- buyer_id: 购买者ID
- post_id: 帖子ID
- purchase_price: 购买价格
- created_at: 创建时间

### 用户活动表 (user_activities)
- id: 活动ID
- user_id: 用户ID
- activity_type: 活动类型
- points_earned: 获得积分
- created_at: 创建时间

## API 接口

### 认证接口
- POST /api/auth/register - 用户注册
- POST /api/auth/login - 用户登录
- GET /api/auth/me - 获取用户信息
- PUT /api/auth/profile - 更新用户资料

### 帖子接口
- GET /api/posts - 获取帖子列表
- POST /api/posts - 创建帖子
- GET /api/posts/{id} - 获取帖子详情
- PUT /api/posts/{id} - 更新帖子
- DELETE /api/posts/{id} - 删除帖子
- POST /api/posts/{id}/like - 点赞/取消点赞
- POST /api/posts/{id}/purchase - 购买帖子

### 评论接口
- POST /api/comments - 创建评论
- GET /api/comments/post/{postId} - 获取帖子评论
- PUT /api/comments/{id} - 更新评论
- DELETE /api/comments/{id} - 删除评论

### 活动接口
- POST /api/activities/daily-sign-in - 每日签到
- GET /api/activities/my-activities - 获取用户活动
- GET /api/activities/learning-coin-ranking - 学习币排行榜

## 开发说明

### 代码结构
- **Controller层**: 处理HTTP请求，调用Service层
- **Service层**: 业务逻辑处理，调用Repository层
- **Repository层**: 数据访问操作
- **Entity层**: 数据库实体类
- **DTO层**: 数据传输对象

### 安全特性
- JWT认证
- 密码加密存储
- 权限控制
- SQL注入防护

### 性能优化
- 分页查询
- 缓存机制
- 数据库索引

## 部署

### 后端打包
```bash
cd backend
mvn clean package
```

### 前端打包
```bash
cd frontend
npm run build
```

## Git分支管理

当前项目分支：
- **master**: 主分支，稳定版本
- **claude**: 开发分支，用于后续开发和维护

### 贡献指南

1. 切换到 claude 分支：`git checkout claude`
2. 创建功能分支：`git checkout -b feature/your-feature`
3. 提交更改
4. 推送到分支：`git push origin feature/your-feature`
5. 创建 Pull Request

## 许可证

MIT License