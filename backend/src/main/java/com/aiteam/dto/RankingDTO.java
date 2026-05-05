package com.aiteam.dto;

import java.time.LocalDateTime;

public class RankingDTO {
    private Long userId;
    private String username;
    private String fullName;
    private String avatarUrl;
    private Long learningCoins;
    private Integer totalSignDays;
    private Integer rank;
    private String rankIcon;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Long getLearningCoins() { return learningCoins; }
    public void setLearningCoins(Long learningCoins) { this.learningCoins = learningCoins; }
    public Integer getTotalSignDays() { return totalSignDays; }
    public void setTotalSignDays(Integer totalSignDays) { this.totalSignDays = totalSignDays; }
    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
    public String getRankIcon() { return rankIcon; }
    public void setRankIcon(String rankIcon) { this.rankIcon = rankIcon; }

    // 设置排名图标
    public void setRankIconByRank() {
        if (rank == 1) {
            this.rankIcon = "🥇";
        } else if (rank == 2) {
            this.rankIcon = "🥈";
        } else if (rank == 3) {
            this.rankIcon = "🥉";
        } else {
            this.rankIcon = "#" + rank;
        }
    }
}