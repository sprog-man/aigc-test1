-- AI学习交流网站数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ai_learning_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_learning_platform;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(50) NULL,
    avatar_url VARCHAR(255) NULL,
    learning_coins BIGINT DEFAULT 100 NOT NULL,
    total_sign_days INT DEFAULT 0 NOT NULL,
    last_sign_date DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL,
    last_login DATETIME NULL,
    enabled BOOLEAN DEFAULT TRUE NOT NULL,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_learning_coins (learning_coins DESC),
    INDEX idx_total_sign_days (total_sign_days DESC),
    INDEX idx_created_at (created_at DESC)
);

-- 用户角色表
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 帖子表
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    price DECIMAL(10,2) DEFAULT 0.00 NOT NULL,
    view_count BIGINT DEFAULT 0 NOT NULL,
    like_count BIGINT DEFAULT 0 NOT NULL,
    purchase_count BIGINT DEFAULT 0 NOT NULL,
    is_pinned BOOLEAN DEFAULT FALSE NOT NULL,
    is_approved BOOLEAN DEFAULT TRUE NOT NULL,
    author_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_is_approved (is_approved),
    INDEX idx_author_id (author_id),
    INDEX idx_created_at (created_at DESC),
    INDEX idx_view_count (view_count DESC),
    INDEX idx_like_count (like_count DESC),
    INDEX idx_purchase_count (purchase_count DESC),
    INDEX idx_is_pinned (is_pinned, created_at DESC)
);

-- 评论表
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    post_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    parent_id BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE CASCADE,
    INDEX idx_post_id (post_id),
    INDEX idx_author_id (author_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_created_at (created_at DESC)
);

-- 购买记录表
CREATE TABLE IF NOT EXISTS post_purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_price DECIMAL(10,2) NOT NULL,
    buyer_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    UNIQUE KEY unique_purchase (buyer_id, post_id),
    INDEX idx_buyer_id (buyer_id),
    INDEX idx_post_id (post_id),
    INDEX idx_created_at (created_at DESC)
);

-- 用户活动表
CREATE TABLE IF NOT EXISTS user_activities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_type ENUM('DAILY_SIGN_IN', 'VIEW_POST', 'COMMENT_POST', 'CREATE_POST', 'LIKE_POST') NOT NULL,
    points_earned INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_activity_type (activity_type),
    INDEX idx_created_at (created_at DESC)
);

-- 插入默认管理员用户
INSERT INTO users (username, password, email, full_name, learning_coins, total_sign_days, created_at, enabled)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi', 'admin@example.com', '管理员', 10000, 0, NOW(), TRUE);

-- 插入管理员角色
INSERT INTO user_roles (user_id, role) VALUES (1, 'ADMIN');

-- 插入示例用户
INSERT INTO users (username, password, email, full_name, learning_coins, total_sign_days, created_at, enabled)
VALUES ('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi', 'user1@example.com', '示例用户1', 500, 5, NOW(), TRUE),
       ('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi', 'user2@example.com', '示例用户2', 300, 3, NOW(), TRUE);

-- 插入示例帖子
INSERT INTO posts (title, content, price, author_id, created_at, is_approved)
VALUES ('机器学习入门基础', '机器学习是人工智能的一个分支，它使计算机能够从数据中学习并做出预测。本文将介绍机器学习的基本概念和常用算法。', 10.00, 1, NOW(), TRUE),
       ('深度学习框架对比', 'TensorFlow、PyTorch、Keras等深度学习框架各有特点，本文将详细对比它们的优缺点和适用场景。', 20.00, 2, NOW(), TRUE),
       ('自然语言处理技术', 'NLP是人工智能的重要领域，本文将介绍最新的NLP技术包括Transformer架构和BERT模型。', 15.00, 1, NOW(), TRUE);

-- 插入示例评论
INSERT INTO comments (content, post_id, author_id, created_at)
VALUES ('很好的文章，学到了很多！', 1, 2, NOW()),
       ('期待更多相关的技术分享', 1, 2, NOW()),
       ('深度学习确实很重要，感谢分享', 2, 1, NOW()),
       ('文章内容很详细，对新手很友好', 3, 1, NOW());

-- 插入示例活动记录
INSERT INTO user_activities (user_id, activity_type, points_earned, created_at)
VALUES (1, 'DAILY_SIGN_IN', 10, NOW()),
       (1, 'VIEW_POST', 1, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
       (1, 'COMMENT_POST', 5, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
       (1, 'CREATE_POST', 20, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
       (2, 'DAILY_SIGN_IN', 10, DATE_SUB(NOW(), INTERVAL 1 DAY)),
       (2, 'LIKE_POST', 2, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
       (2, 'VIEW_POST', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
       (2, 'DAILY_SIGN_IN', 10, DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 插入示例购买记录
INSERT INTO post_purchases (purchase_price, buyer_id, post_id, created_at)
VALUES (10.00, 2, 1, NOW()),
       (20.00, 1, 2, DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 创建索引优化查询性能
CREATE INDEX idx_posts_author_created ON posts(author_id, created_at DESC);
CREATE INDEX idx_comments_post_created ON comments(post_id, created_at DESC);
CREATE INDEX idx_activities_user_created ON user_activities(user_id, created_at DESC);