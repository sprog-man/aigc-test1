package com.aiteam.service;

import com.aiteam.dto.ActivityDTO;
import com.aiteam.dto.RankingDTO;
import com.aiteam.entity.User;
import com.aiteam.entity.UserActivity;
import com.aiteam.repository.UserRepository;
import com.aiteam.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserActivityService {

    private final UserActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityDTO.CreateResponse createActivity(Long userId, UserActivity.ActivityType activityType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setActivityType(activityType);
        activity.setPointsEarned(activityType.getPoints());
        activity.setCreatedAt(LocalDateTime.now());

        // 更新用户相关数据
        switch (activityType) {
            case DAILY_SIGN_IN:
                handleDailySignIn(user);
                break;
            case VIEW_POST:
                handleViewPost(user);
                break;
            case COMMENT_POST:
                handleCommentPost(user);
                break;
            case CREATE_POST:
                handleCreatePost(user);
                break;
            case LIKE_POST:
                handleLikePost(user);
                break;
        }

        UserActivity savedActivity = activityRepository.save(activity);
        return convertToCreateResponse(savedActivity);
    }

    private void handleDailySignIn(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastSignDate = user.getLastSignDate();

        // 检查今天是否已经签到
        if (lastSignDate == null || lastSignDate.toLocalDate().isBefore(now.toLocalDate())) {
            user.setLastSignDate(now);
            user.setTotalSignDays(user.getTotalSignDays() + 1);
            userRepository.save(user);
        } else {
            throw new RuntimeException("今天已经签到过了");
        }
    }

    private void handleViewPost(User user) {
        // 浏览帖子的奖励逻辑
        // 这里可以添加防刷机制
        user.addLearningCoins(1L);  // 1学习币
        userRepository.save(user);
    }

    private void handleCommentPost(User user) {
        // 发布评论的奖励逻辑
        user.addLearningCoins(5L);  // 5学习币
        userRepository.save(user);
    }

    private void handleCreatePost(User user) {
        // 发布帖子的奖励逻辑
        user.addLearningCoins(20L);  // 20学习币
        userRepository.save(user);
    }

    private void handleLikePost(User user) {
        // 点赞帖子的奖励逻辑
        user.addLearningCoins(2L);  // 2学习币
        userRepository.save(user);
    }

    public PageResult<ActivityDTO.GetResponse> getUserActivities(Long userId, Pageable pageable) {
        Page<UserActivity> activities = activityRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        List<ActivityDTO.GetResponse> responseList = activities.getContent().stream()
                .map(this::convertToGetResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, activities.getTotalElements(),
                activities.getNumber() + 1, activities.getSize());
    }

    public PageResult<ActivityDTO.GetResponse> getUserActivitiesByType(Long userId, UserActivity.ActivityType activityType, Pageable pageable) {
        Page<UserActivity> activities = activityRepository.findByUserIdAndActivityType(userId, activityType, pageable);
        List<ActivityDTO.GetResponse> responseList = activities.getContent().stream()
                .map(this::convertToGetResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, activities.getTotalElements(),
                activities.getNumber() + 1, activities.getSize());
    }

    public List<ActivityDTO.GetResponse> getRecentActivities(Long userId, int limit) {
        List<UserActivity> activities = activityRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return activities.stream()
                .limit(limit)
                .map(this::convertToGetResponse)
                .collect(Collectors.toList());
    }

    public RankingDTO.GetResponse getLearningCoinRanking(int topN) {
        List<User> users = userRepository.findAllByOrderByLearningCoinsDesc();
        List<User> topUsers = users.stream()
                .limit(topN)
                .collect(Collectors.toList());

        List<RankingDTO.GetResponse> rankingList = topUsers.stream()
                .map(user -> convertToRankingResponse(user, topUsers.indexOf(user) + 1))
                .collect(Collectors.toList());

        RankingDTO.GetResponse response = new RankingDTO.GetResponse();
        response.setData(rankingList);
        response.setTotal(users.size());
        response.setTop(topN);

        return response;
    }

    public RankingDTO.GetResponse getUserLearningRanking(Long userId) {
        List<User> users = userRepository.findAllByOrderByLearningCoinsDesc();
        int rank = users.stream()
                .mapToLong(User::getId)
                .boxed()
                .toList()
                .indexOf(userId) + 1;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<RankingDTO.GetResponse> rankingList = users.stream()
                .limit(50)  // 只取前50名
                .map(u -> convertToRankingResponse(u, users.indexOf(u) + 1))
                .collect(Collectors.toList());

        RankingDTO.GetResponse response = new RankingDTO.GetResponse();
        response.setData(rankingList);
        response.setTotal(users.size());
        response.setUserRank(rank);

        return response;
    }

    public RankingDTO.GetResponse getSignDayRanking(int topN) {
        List<User> users = userRepository.findAllByOrderByTotalSignDaysDesc();
        List<User> topUsers = users.stream()
                .limit(topN)
                .collect(Collectors.toList());

        List<RankingDTO.GetResponse> rankingList = topUsers.stream()
                .map(user -> convertToRankingResponse(user, topUsers.indexOf(user) + 1))
                .collect(Collectors.toList());

        RankingDTO.GetResponse response = new RankingDTO.GetResponse();
        response.setData(rankingList);
        response.setTotal(users.size());
        response.setTop(topN);

        return response;
    }

    public RankingDTO.GetResponse getUserSignDayRanking(Long userId) {
        List<User> users = userRepository.findAllByOrderByTotalSignDaysDesc();
        int rank = users.stream()
                .mapToLong(User::getId)
                .boxed()
                .toList()
                .indexOf(userId) + 1;

        List<RankingDTO.GetResponse> rankingList = users.stream()
                .limit(50)  // 只取前50名
                .map(u -> convertToRankingResponse(u, users.indexOf(u) + 1))
                .collect(Collectors.toList());

        RankingDTO.GetResponse response = new RankingDTO.GetResponse();
        response.setData(rankingList);
        response.setTotal(users.size());
        response.setUserRank(rank);

        return response;
    }

    private ActivityDTO.CreateResponse convertToCreateResponse(UserActivity activity) {
        ActivityDTO.CreateResponse response = new ActivityDTO.CreateResponse();
        response.setId(activity.getId());
        response.setActivityType(activity.getActivityType().name());
        response.setPointsEarned(activity.getPointsEarned());
        response.setCreatedAt(activity.getCreatedAt());
        response.setActivityDescription(getActivityDescription(activity.getActivityType()));
        return response;
    }

    private ActivityDTO.GetResponse convertToGetResponse(UserActivity activity) {
        ActivityDTO.GetResponse response = new ActivityDTO.GetResponse();
        response.setId(activity.getId());
        response.setActivityType(activity.getActivityType().name());
        response.setPointsEarned(activity.getPointsEarned());
        response.setCreatedAt(activity.getCreatedAt());
        response.setActivityDescription(getActivityDescription(activity.getActivityType()));
        return response;
    }

    private RankingDTO.GetResponse convertToRankingResponse(User user, int rank) {
        RankingDTO.GetResponse response = new RankingDTO.GetResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setLearningCoins(user.getLearningCoins());
        response.setTotalSignDays(user.getTotalSignDays());
        response.setRank(rank);
        response.setRankIcon(getRankIcon(rank));
        return response;
    }

    private String getActivityDescription(UserActivity.ActivityType activityType) {
        return switch (activityType) {
            case DAILY_SIGN_IN -> "每日签到";
            case VIEW_POST -> "浏览帖子";
            case COMMENT_POST -> "发布评论";
            case CREATE_POST -> "发布帖子";
            case LIKE_POST -> "点赞帖子";
        };
    }

    private String getRankIcon(int rank) {
        return switch (rank) {
            case 1 -> "🥇";
            case 2 -> "🥈";
            case 3 -> "🥉";
            default -> "#" + rank;
        };
    }

    // DTO内部类
    public static class CreateResponse extends ActivityDTO {
        private String activityDescription;
        // Getters and Setters
        public String getActivityDescription() { return activityDescription; }
        public void setActivityDescription(String activityDescription) { this.activityDescription = activityDescription; }
    }

    public static class GetResponse extends ActivityDTO {
        private String activityDescription;
        // Getters and Setters
        public String getActivityDescription() { return activityDescription; }
        public void setActivityDescription(String activityDescription) { this.activityDescription = activityDescription; }
    }

    public static class GetRankingResponse extends RankingDTO {
        private List<RankingDTO.GetResponse> data;
        private Integer total;
        private Integer top;
        private Integer userRank;
        // Getters and Setters
        public List<RankingDTO.GetResponse> getData() { return data; }
        public void setData(List<RankingDTO.GetResponse> data) { this.data = data; }
        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
        public Integer getTop() { return top; }
        public void setTop(Integer top) { this.top = top; }
        public Integer getUserRank() { return userRank; }
        public void setUserRank(Integer userRank) { this.userRank = userRank; }
    }
}