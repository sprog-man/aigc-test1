package com.aiteam.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activities")
@Data
@NoArgsConstructor
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    @Column(name = "points_earned", nullable = false)
    private Integer pointsEarned;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public enum ActivityType {
        DAILY_SIGN_IN(10),
        VIEW_POST(1),
        COMMENT_POST(5),
        CREATE_POST(20),
        LIKE_POST(2);

        private final int points;

        ActivityType(int points) {
            this.points = points;
        }

        public int getPoints() {
            return points;
        }
    }
}