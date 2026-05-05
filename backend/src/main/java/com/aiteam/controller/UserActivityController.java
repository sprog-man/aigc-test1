package com.aiteam.controller;

import com.aiteam.dto.*;
import com.aiteam.dto.PageResult;
import com.aiteam.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService activityService;

    // 每日签到
    @PostMapping("/daily-sign-in")
    public ResponseEntity<Response<ActivityDTO.CreateResponse>> dailySignIn(
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            ActivityDTO.CreateResponse response = activityService.createActivity(userId, UserActivity.ActivityType.DAILY_SIGN_IN);
            return ResponseEntity.ok(Response.success(response, "签到成功，获得10学习币"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取用户活动记录
    @GetMapping("/my-activities")
    public ResponseEntity<Response<PageResult<ActivityDTO.GetResponse>>> getMyActivities(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Long userId = extractUserIdFromToken(token);
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PageResult<ActivityDTO.GetResponse> result = activityService.getUserActivities(userId, pageable);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取用户活动记录（按类型）
    @GetMapping("/my-activities/{type}")
    public ResponseEntity<Response<PageResult<ActivityDTO.GetResponse>>> getMyActivitiesByType(
            @PathVariable String type,
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Long userId = extractUserIdFromToken(token);
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            UserActivity.ActivityType activityType = UserActivity.ActivityType.valueOf(type);
            PageResult<ActivityDTO.GetResponse> result = activityService.getUserActivitiesByType(userId, activityType, pageable);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取用户最近活动
    @GetMapping("/my-recent-activities")
    public ResponseEntity<Response<List<ActivityDTO.GetResponse>>> getMyRecentActivities(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Long userId = extractUserIdFromToken(token);
            List<ActivityDTO.GetResponse> result = activityService.getRecentActivities(userId, limit);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取学习币排行榜
    @GetMapping("/learning-coin-ranking")
    public ResponseEntity<Response<RankingDTO.GetRankingResponse>> getLearningCoinRanking(
            @RequestParam(defaultValue = "50") int topN) {
        try {
            RankingDTO.GetRankingResponse result = activityService.getLearningCoinRanking(topN);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取用户学习币排名
    @GetMapping("/my-learning-rank")
    public ResponseEntity<Response<RankingDTO.GetRankingResponse>> getMyLearningRanking(
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            RankingDTO.GetRankingResponse result = activityService.getUserLearningRanking(userId);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取签到天数排行榜
    @GetMapping("/sign-day-ranking")
    public ResponseEntity<Response<RankingDTO.GetRankingResponse>> getSignDayRanking(
            @RequestParam(defaultValue = "50") int topN) {
        try {
            RankingDTO.GetRankingResponse result = activityService.getSignDayRanking(topN);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取用户签到天数排名
    @GetMapping("/my-sign-rank")
    public ResponseEntity<Response<RankingDTO.GetRankingResponse>> getMySignRanking(
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            RankingDTO.GetRankingResponse result = activityService.getUserSignDayRanking(userId);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取用户当前学习币余额
    @GetMapping("/my-balance")
    public ResponseEntity<Response<Long>> getMyBalance(@RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            Long balance = activityService.getUserBalance(userId);
            return ResponseEntity.ok(Response.success(balance));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    // 获取用户签到状态
    @GetMapping("/my-sign-status")
    public ResponseEntity<Response<SignStatusResponse>> getMySignStatus(@RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            SignStatusResponse status = activityService.getSignInStatus(userId);
            return ResponseEntity.ok(Response.success(status));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    private Long extractUserIdFromToken(String token) {
        // 这里应该解析JWT token获取用户ID
        // 简化处理，实际需要完整的JWT解析逻辑
        return 1L; // 临时返回，需要完善
    }

    // 签到状态响应
    public static class SignStatusResponse {
        private boolean canSignIn;
        private boolean hasSignedToday;
        private String lastSignDate;
        private Long totalSignDays;

        public boolean isCanSignIn() { return canSignIn; }
        public void setCanSignIn(boolean canSignIn) { this.canSignIn = canSignIn; }
        public boolean isHasSignedToday() { return hasSignedToday; }
        public void setHasSignedToday(boolean hasSignedToday) { this.hasSignedToday = hasSignedToday; }
        public String getLastSignDate() { return lastSignDate; }
        public void setLastSignDate(String lastSignDate) { this.lastSignDate = lastSignDate; }
        public Long getTotalSignDays() { return totalSignDays; }
        public void setTotalSignDays(Long totalSignDays) { this.totalSignDays = totalSignDays; }
    }
}