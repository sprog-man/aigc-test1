package com.aiteam.controller;

import com.aiteam.dto.*;
import com.aiteam.dto.Response;
import com.aiteam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 获取用户信息
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserService.UserProfileResponse>> getUserProfile(
            @PathVariable Long userId) {
        try {
            UserService.UserProfileResponse profile = userService.getUserProfile(userId);
            return ResponseEntity.ok(Response.success(profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 更新用户信息
    @PutMapping("/{userId}")
    public ResponseEntity<Response<UserService.UserProfileResponse>> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserDTO.UpdateRequest request) {
        try {
            UserService.UserProfileResponse profile = userService.updateUserProfile(userId, request);
            return ResponseEntity.ok(Response.success(profile, "更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 修改密码
    @PutMapping("/{userId}/password")
    public ResponseEntity<Response<String>> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok(Response.success("密码修改成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取用户学习币余额
    @GetMapping("/{userId}/learning-coins")
    public ResponseEntity<Response<Long>> getUserLearningCoins(@PathVariable Long userId) {
        try {
            Long balance = userService.getUserLearningCoins(userId);
            return ResponseEntity.ok(Response.success(balance));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 检查用户是否有足够的学习币
    @GetMapping("/{userId}/check-balance")
    public ResponseEntity<Response<BalanceCheckResponse>> checkUserBalance(
            @PathVariable Long userId,
            @RequestParam Long amount) {
        try {
            boolean hasEnough = userService.hasEnoughLearningCoins(userId, amount);
            BalanceCheckResponse response = new BalanceCheckResponse();
            response.setHasEnough(hasEnough);
            response.setCurrentBalance(userService.getUserLearningCoins(userId));
            response.setRequiredAmount(amount);
            return ResponseEntity.ok(Response.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 检查用户名邮箱是否可用
    @PostMapping("/check-username-email")
    public ResponseEntity<Response<CheckResponse>> checkUsernameEmail(
            @Valid @RequestBody CheckUsernameEmailRequest request) {
        try {
            boolean usernameExists = userService.checkIfUserExists(request.getUsername(), null);
            boolean emailExists = userService.checkIfUserExists(null, request.getEmail());

            CheckResponse response = new CheckResponse();
            response.setUsernameExists(usernameExists);
            response.setEmailExists(emailExists);
            response.setAvailable(!usernameExists && !emailExists);

            return ResponseEntity.ok(Response.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 修改密码请求
    public static class ChangePasswordRequest {
        @NotBlank(message = "原密码不能为空")
        private String oldPassword;

        @NotBlank(message = "新密码不能为空")
        @Size(min = 6, message = "新密码长度不能少于6个字符")
        private String newPassword;

        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    // 余额检查响应
    public static class BalanceCheckResponse {
        private boolean hasEnough;
        private Long currentBalance;
        private Long requiredAmount;

        public boolean isHasEnough() { return hasEnough; }
        public void setHasEnough(boolean hasEnough) { this.hasEnough = hasEnough; }
        public Long getCurrentBalance() { return currentBalance; }
        public void setCurrentBalance(Long currentBalance) { this.currentBalance = currentBalance; }
        public Long getRequiredAmount() { return requiredAmount; }
        public void setRequiredAmount(Long requiredAmount) { this.requiredAmount = requiredAmount; }
    }

    // 检查响应
    public static class CheckResponse {
        private boolean usernameExists;
        private boolean emailExists;
        private boolean available;

        public boolean isUsernameExists() { return usernameExists; }
        public void setUsernameExists(boolean usernameExists) { this.usernameExists = usernameExists; }
        public boolean isEmailExists() { return emailExists; }
        public void setEmailExists(boolean emailExists) { this.emailExists = emailExists; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
    }

    // 检查用户名邮箱请求
    public static class CheckUsernameEmailRequest {
        private String username;
        private String email;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}