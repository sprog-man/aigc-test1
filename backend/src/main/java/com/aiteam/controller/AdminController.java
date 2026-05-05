package com.aiteam.controller;

import com.aiteam.dto.*;
import com.aiteam.dto.Response;
import com.aiteam.entity.Post;
import com.aiteam.repository.PostRepository;
import com.aiteam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PostRepository postRepository;
    private final UserService userService;

    // 获取所有用户
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<PageResult<UserService.UserProfileResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            // 这里需要创建对应的Service方法
            // Page<User> users = userService.getAllUsers(pageable);
            // PageResult<UserService.UserProfileResponse> result = convertToPageResult(users);

            return ResponseEntity.ok(Response.success(null, "获取用户列表成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取用户统计信息
    @GetMapping("/users/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<UserStatsResponse>> getUserStats() {
        try {
            long totalUsers = userService.getTotalUsers();
            long activeUsers = userService.getActiveUsers();
            long newUsersToday = userService.getNewUsersToday();
            long newUsersThisMonth = userService.getNewUsersThisMonth();

            UserStatsResponse stats = new UserStatsResponse();
            stats.setTotalUsers(totalUsers);
            stats.setActiveUsers(activeUsers);
            stats.setNewUsersToday(newUsersToday);
            stats.setNewUsersThisMonth(newUsersThisMonth);

            return ResponseEntity.ok(Response.success(stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 管理帖子审核
    @PutMapping("/posts/{postId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<PostDTO.GetResponse>> approvePost(
            @PathVariable Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("帖子不存在"));

            post.setIsApproved(true);
            Post savedPost = postRepository.save(post);

            PostDTO.GetResponse response = convertToGetResponse(savedPost);
            return ResponseEntity.ok(Response.success(response, "帖子审核通过"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 管理帖子拒绝
    @PutMapping("/posts/{postId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<String>> rejectPost(
            @PathVariable Long postId,
            @RequestBody RejectPostRequest request) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("帖子不存在"));

            post.setIsApproved(false);
            postRepository.save(post);

            return ResponseEntity.ok(Response.success("帖子已拒绝"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取待审核帖子
    @GetMapping("/posts/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<PageResult<PostDTO.GetResponse>>> getPendingPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<Post> posts = postRepository.findByIsApprovedFalseOrderByCreatedAtDesc(pageable);
            List<PostDTO.GetResponse> responseList = posts.getContent().stream()
                    .map(this::convertToGetResponse)
                    .toList();

            PageResult<PostDTO.GetResponse> result = PageResult.of(responseList, posts.getTotalElements(), page + 1, size);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取平台统计
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<PlatformStatsResponse>> getPlatformStats() {
        try {
            long totalPosts = postRepository.count();
            long approvedPosts = postRepository.countByIsApprovedTrue();
            long pendingPosts = postRepository.countByIsApprovedFalse();
            long totalUsers = userService.getTotalUsers();

            // 获取热门帖子
            Page<Post> popularPosts = postRepository.findByIsApprovedTrueOrderByViewCountDesc(PageRequest.of(0, 5));
            List<PostDTO.GetResponse> popularPostsList = popularPosts.getContent().stream()
                    .map(this::convertToGetResponse)
                    .toList();

            PlatformStatsResponse stats = new PlatformStatsResponse();
            stats.setTotalPosts(totalPosts);
            stats.setApprovedPosts(approvedPosts);
            stats.setPendingPosts(pendingPosts);
            stats.setTotalUsers(totalUsers);
            stats.setPopularPosts(popularPostsList);

            return ResponseEntity.ok(Response.success(stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 用户封禁/解封
    @PutMapping("/users/{userId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<String>> toggleUserStatus(
            @PathVariable Long userId) {
        try {
            // 这里需要实现用户状态切换逻辑
            // userService.toggleUserStatus(userId);
            return ResponseEntity.ok(Response.success("用户状态切换成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    private PostDTO.GetResponse convertToGetResponse(Post post) {
        PostDTO.GetResponse response = new PostDTO.GetResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setPrice(post.getPrice());
        response.setViewCount(post.getViewCount());
        response.setLikeCount(post.getLikeCount());
        response.setPurchaseCount(post.getPurchaseCount());
        response.setIsPinned(post.getIsPinned());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setAuthorId(post.getAuthor().getId());
        response.setAuthorName(post.getAuthor().getUsername());
        return response;
    }

    // 统计响应
    public static class UserStatsResponse {
        private long totalUsers;
        private long activeUsers;
        private long newUsersToday;
        private long newUsersThisMonth;

        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }
        public long getNewUsersToday() { return newUsersToday; }
        public void setNewUsersToday(long newUsersToday) { this.newUsersToday = newUsersToday; }
        public long getNewUsersThisMonth() { return newUsersThisMonth; }
        public void setNewUsersThisMonth(long newUsersThisMonth) { this.newUsersThisMonth = newUsersThisMonth; }
    }

    // 平台统计响应
    public static class PlatformStatsResponse {
        private long totalPosts;
        private long approvedPosts;
        private long pendingPosts;
        private long totalUsers;
        private List<PostDTO.GetResponse> popularPosts;

        public long getTotalPosts() { return totalPosts; }
        public void setTotalPosts(long totalPosts) { this.totalPosts = totalPosts; }
        public long getApprovedPosts() { return approvedPosts; }
        public void setApprovedPosts(long approvedPosts) { this.approvedPosts = approvedPosts; }
        public long getPendingPosts() { return pendingPosts; }
        public void setPendingPosts(long pendingPosts) { this.pendingPosts = pendingPosts; }
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public List<PostDTO.GetResponse> getPopularPosts() { return popularPosts; }
        public void setPopularPosts(List<PostDTO.GetResponse> popularPosts) { this.popularPosts = popularPosts; }
    }

    // 拒绝帖子请求
    public static class RejectPostRequest {
        private String reason;

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}