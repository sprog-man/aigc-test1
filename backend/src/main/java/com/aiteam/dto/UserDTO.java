package com.aiteam.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String avatarUrl;
    private Long learningCoins;
    private Integer totalSignDays;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    // 注册请求
    public static class RegisterRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
        private String username;

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, message = "密码长度不能少于6个字符")
        private String password;

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        @Size(max = 50, message = "姓名长度不能超过50个字符")
        private String fullName;

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
    }

    // 登录请求
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // 更新用户信息
    public static class UpdateRequest {
        @Size(max = 50, message = "姓名长度不能超过50个字符")
        private String fullName;

        @Size(max = 255, message = "头像URL长度不能超过255个字符")
        private String avatarUrl;

        // Getters and Setters
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Long getLearningCoins() { return learningCoins; }
    public void setLearningCoins(Long learningCoins) { this.learningCoins = learningCoins; }
    public Integer getTotalSignDays() { return totalSignDays; }
    public void setTotalSignDays(Integer totalSignDays) { this.totalSignDays = totalSignDays; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
}