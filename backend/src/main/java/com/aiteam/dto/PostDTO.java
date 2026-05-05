package com.aiteam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private BigDecimal price;
    private Long viewCount;
    private Long likeCount;
    private Long purchaseCount;
    private Boolean isPinned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long authorId;
    private String authorName;
    private Boolean isPurchasedByCurrentUser;

    // 创建帖子请求
    public static class CreateRequest {
        @NotBlank(message = "标题不能为空")
        @Size(max = 100, message = "标题长度不能超过100个字符")
        private String title;

        @NotBlank(message = "内容不能为空")
        @Size(min = 10, max = 10000, message = "内容长度必须在10-10000个字符之间")
        private String content;

        @NotNull(message = "价格不能为空")
        private BigDecimal price;

        private Boolean isPinned = false;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Boolean getPinned() { return isPinned; }
        public void setPinned(Boolean pinned) { isPinned = pinned; }
    }

    // 更新帖子请求
    public static class UpdateRequest {
        @NotBlank(message = "标题不能为空")
        @Size(max = 100, message = "标题长度不能超过100个字符")
        private String title;

        @NotBlank(message = "内容不能为空")
        @Size(min = 10, max = 10000, message = "内容长度必须在10-10000个字符之间")
        private String content;

        @NotNull(message = "价格不能为空")
        private BigDecimal price;

        private Boolean isPinned;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Boolean getPinned() { return isPinned; }
        public void setPinned(Boolean pinned) { isPinned = pinned; }
    }

    // 点赞请求
    public static class LikeRequest {
        private Boolean like;

        // Getters and Setters
        public Boolean getLike() { return like; }
        public void setLike(Boolean like) { this.like = like; }
    }

    // 购买帖子请求
    public static class PurchaseRequest {
        private Long postId;

        // Getters and Setters
        public Long getPostId() { return postId; }
        public void setPostId(Long postId) { this.postId = postId; }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public Long getPurchaseCount() { return purchaseCount; }
    public void setPurchaseCount(Long purchaseCount) { this.purchaseCount = purchaseCount; }
    public Boolean getPinned() { return isPinned; }
    public void setPinned(Boolean pinned) { isPinned = pinned; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public Boolean getPurchasedByCurrentUser() { return isPurchasedByCurrentUser; }
    public void setPurchasedByCurrentUser(Boolean purchasedByCurrentUser) { this.isPurchasedByCurrentUser = purchasedByCurrentUser; }
}