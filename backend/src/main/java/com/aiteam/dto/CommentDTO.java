package com.aiteam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long authorId;
    private String authorName;
    private Long postId;
    private Long parentId;

    // 创建评论请求
    public static class CreateRequest {
        @NotBlank(message = "评论内容不能为空")
        @Size(min = 1, max = 1000, message = "评论长度不能超过1000个字符")
        private String content;

        private Long parentId;  // 回复评论的ID

        // Getters and Setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
    }

    // 更新评论请求
    public static class UpdateRequest {
        @NotBlank(message = "评论内容不能为空")
        @Size(min = 1, max = 1000, message = "评论长度不能超过1000个字符")
        private String content;

        // Getters and Setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}