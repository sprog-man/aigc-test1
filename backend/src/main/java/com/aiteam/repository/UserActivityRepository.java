package com.aiteam.repository;

import com.aiteam.entity.UserActivity;
import com.aiteam.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    Page<UserActivity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    List<UserActivity> findByUserIdAndActivityType(Long userId, UserActivity.ActivityType activityType);

    @Query("SELECT ua FROM UserActivity ua WHERE ua.user = :user AND ua.activityType = :activityType AND ua.createdAt >= :date")
    List<UserActivity> findByUserAndActivityTypeAfterDate(@Param("user") User user,
                                                        @Param("activityType") UserActivity.ActivityType activityType,
                                                        @Param("date") LocalDateTime date);

    @Query("SELECT ua FROM UserActivity ua WHERE ua.user = :user")
    List<UserActivity> findByUser(@Param("user") User user);
}