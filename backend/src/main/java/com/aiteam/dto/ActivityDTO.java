package com.aiteam.dto;

import com.aiteam.entity.UserActivity;
import java.time.LocalDateTime;

public class ActivityDTO {
    private Long id;
    private String activityType;
    private Integer pointsEarned;
    private LocalDateTime createdAt;
    private String activityDescription;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getActivityDescription() { return activityDescription; }
    public void setActivityDescription(String activityDescription) { this.activityDescription = activityDescription; }

    // 静态方法用于创建ActivityDTO
    public static ActivityDTO fromActivity(UserActivity activity) {
        ActivityDTO dto = new ActivityDTO();
        dto.setId(activity.getId());
        dto.setActivityType(activity.getActivityType().name());
        dto.setPointsEarned(activity.getPointsEarned());
        dto.setCreatedAt(activity.getCreatedAt());

        // 设置活动描述
        switch (activity.getActivityType()) {
            case DAILY_SIGN_IN:
                dto.setActivityDescription("每日签到");
                break;
            case VIEW_POST:
                dto.setActivityDescription("浏览帖子");
                break;
            case COMMENT_POST:
                dto.setActivityDescription("发布评论");
                break;
            case CREATE_POST:
                dto.setActivityDescription("发布帖子");
                break;
            case LIKE_POST:
                dto.setActivityDescription("点赞帖子");
                break;
        }

        return dto;
    }
}