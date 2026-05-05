package com.aiteam.service;

import com.aiteam.dto.UserDTO;
import com.aiteam.entity.User;
import com.aiteam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }

    public User createUser(UserDTO.RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setLearningCoins(100L);  // 初始100学习币
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public UserDTO.UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        UserDTO.UserProfileResponse response = new UserDTO.UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setLearningCoins(user.getLearningCoins());
        response.setTotalSignDays(user.getTotalSignDays());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastLogin(user.getLastLogin());

        return response;
    }

    public UserDTO.UserProfileResponse updateUserProfile(Long userId, UserDTO.UpdateRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        user.setFullName(updateRequest.getFullName());
        user.setAvatarUrl(updateRequest.getAvatarUrl());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return getUserProfile(updatedUser.getId());
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void updateLastLogin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    public boolean checkIfUserExists(String username, String email) {
        boolean usernameExists = userRepository.existsByUsername(username);
        boolean emailExists = userRepository.existsByEmail(email);
        return usernameExists || emailExists;
    }

    // 获取用户余额
    public Long getUserLearningCoins(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        return user.getLearningCoins();
    }

    // 检查用户是否有足够的学习币
    public boolean hasEnoughLearningCoins(Long userId, Long amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        return user.getLearningCoins() >= amount;
    }

    // 扣除学习币
    public void deductLearningCoins(Long userId, Long amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        if (user.getLearningCoins() < amount) {
            throw new RuntimeException("学习币余额不足");
        }

        user.deductLearningCoins(amount);
        userRepository.save(user);
    }

    // 增加学习币
    public void addLearningCoins(Long userId, Long amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        user.addLearningCoins(amount);
        userRepository.save(user);
    }

    // 用户注册响应
    public static class UserProfileResponse {
        private Long id;
        private String username;
        private String fullName;
        private String email;
        private String avatarUrl;
        private Long learningCoins;
        private Integer totalSignDays;
        private LocalDateTime createdAt;
        private LocalDateTime lastLogin;

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
}