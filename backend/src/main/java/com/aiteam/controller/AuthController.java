package com.aiteam.controller;

import com.aiteam.dto.UserDTO;
import com.aiteam.dto.Response;
import com.aiteam.service.UserService;
import com.aiteam.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Response<UserDTO.UserProfileResponse>> register(@Valid @RequestBody UserDTO.RegisterRequest registerRequest) {
        try {
            User createdUser = userService.createUser(registerRequest);
            UserDTO.UserProfileResponse userProfile = new UserDTO.UserProfileResponse();
            userProfile.setId(createdUser.getId());
            userProfile.setUsername(createdUser.getUsername());
            userProfile.setFullName(createdUser.getFullName());
            userProfile.setEmail(createdUser.getEmail());
            userProfile.setAvatarUrl(createdUser.getAvatarUrl());
            userProfile.setLearningCoins(createdUser.getLearningCoins());
            userProfile.setTotalSignDays(createdUser.getTotalSignDays());
            userProfile.setCreatedAt(createdUser.getCreatedAt());
            userProfile.setLastLogin(createdUser.getLastLogin());

            return ResponseEntity.ok(Response.success(userProfile, "注册成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody UserDTO.LoginRequest loginRequest) {
        try {
            // 验证用户名和密码
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // 加载用户详情
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());

            // 生成JWT token
            String token = jwtUtil.generateToken(userDetails);

            // 获取用户信息
            User user = (User) userDetails;
            UserDTO.UserProfileResponse userProfile = new UserDTO.UserProfileResponse();
            userProfile.setId(user.getId());
            userProfile.setUsername(user.getUsername());
            userProfile.setFullName(user.getFullName());
            userProfile.setEmail(user.getEmail());
            userProfile.setAvatarUrl(user.getAvatarUrl());
            userProfile.setLearningCoins(user.getLearningCoins());
            userProfile.setTotalSignDays(user.getTotalSignDays());
            userProfile.setCreatedAt(user.getCreatedAt());
            userProfile.setLastLogin(user.getLastLogin());

            // 更新最后登录时间
            userService.updateLastLogin(user.getId());

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setUserProfile(userProfile);
            loginResponse.setTokenType("Bearer");
            loginResponse.setExpiresIn(jwtUtil.extractExpiration(token).getTime());

            return ResponseEntity.ok(Response.success(loginResponse, "登录成功"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body(Response.error("用户名或密码错误"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Response<UserDTO.UserProfileResponse>> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            // 提取token中的用户名
            String username = jwtUtil.extractUsername(token.substring(7));
            UserDetails userDetails = userService.loadUserByUsername(username);
            User user = (User) userDetails;

            UserDTO.UserProfileResponse userProfile = new UserDTO.UserProfileResponse();
            userProfile.setId(user.getId());
            userProfile.setUsername(user.getUsername());
            userProfile.setFullName(user.getFullName());
            userProfile.setEmail(user.getEmail());
            userProfile.setAvatarUrl(user.getAvatarUrl());
            userProfile.setLearningCoins(user.getLearningCoins());
            userProfile.setTotalSignDays(user.getTotalSignDays());
            userProfile.setCreatedAt(user.getCreatedAt());
            userProfile.setLastLogin(user.getLastLogin());

            return ResponseEntity.ok(Response.success(userProfile));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Response<UserDTO.UserProfileResponse>> updateProfile(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserDTO.UpdateRequest updateRequest) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            User user = (User) userService.loadUserByUsername(username);

            UserDTO.UserProfileResponse userProfile = userService.updateUserProfile(user.getId(), updateRequest);
            return ResponseEntity.ok(Response.success(userProfile, "更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<Response<String>> changePassword(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            User user = (User) userService.loadUserByUsername(username);

            userService.changePassword(user.getId(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok(Response.success("密码修改成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @PostMapping("/check-username-email")
    public ResponseEntity<Response<CheckResponse>> checkUsernameEmail(
            @Valid @RequestBody CheckUsernameEmailRequest request) {
        try {
            boolean exists = userService.checkIfUserExists(request.getUsername(), request.getEmail());
            CheckResponse response = new CheckResponse();
            response.setExists(exists);
            response.setUsernameExists(userService.checkIfUserExists(request.getUsername(), null));
            response.setEmailExists(userService.checkIfUserExists(null, request.getEmail()));
            return ResponseEntity.ok(Response.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 登录响应
    public static class LoginResponse {
        private String token;
        private String tokenType;
        private Long expiresIn;
        private UserDTO.UserProfileResponse userProfile;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
        public Long getExpiresIn() { return expiresIn; }
        public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
        public UserDTO.UserProfileResponse getUserProfile() { return userProfile; }
        public void setUserProfile(UserDTO.UserProfileResponse userProfile) { this.userProfile = userProfile; }
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

    // 检查用户名邮箱响应
    public static class CheckResponse {
        private boolean exists;
        private boolean usernameExists;
        private boolean emailExists;

        public boolean isExists() { return exists; }
        public void setExists(boolean exists) { this.exists = exists; }
        public boolean isUsernameExists() { return usernameExists; }
        public void setUsernameExists(boolean usernameExists) { this.usernameExists = usernameExists; }
        public boolean isEmailExists() { return emailExists; }
        public void setEmailExists(boolean emailExists) { this.emailExists = emailExists; }
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