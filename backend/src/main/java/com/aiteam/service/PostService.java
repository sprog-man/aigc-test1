package com.aiteam.service;

import com.aiteam.dto.PageResult;
import com.aiteam.dto.PostDTO;
import com.aiteam.entity.Post;
import com.aiteam.entity.PostPurchase;
import com.aiteam.entity.User;
import com.aiteam.repository.PostRepository;
import com.aiteam.repository.PostPurchaseRepository;
import com.aiteam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostPurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    public PostDTO.CreateResponse createPost(Long userId, PostDTO.CreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setPrice(request.getPrice());
        post.setIsPinned(request.getPinned() != null ? request.getPinned() : false);
        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        // 创建用户活动记录
        // createUserActivity(userId, UserActivity.ActivityType.CREATE_POST);

        return convertToCreateResponse(savedPost);
    }

    public PageResult<PostDTO.GetResponse> getPublicPosts(Pageable pageable, Long userId) {
        Page<Post> posts = postRepository.findByIsApprovedTrueOrderByCreatedAtDesc(pageable);
        return convertToPageResult(posts, userId);
    }

    public PageResult<PostDTO.GetResponse> getPostsByAuthor(Long authorId, Pageable pageable, Long currentUserId) {
        Page<Post> posts = postRepository.findByIsApprovedTrueAndAuthorIdOrderByCreatedAtDesc(authorId, pageable);
        return convertToPageResult(posts, currentUserId);
    }

    public PageResult<PostDTO.GetResponse> getSearchPosts(String keyword, Pageable pageable, Long userId) {
        Page<Post> posts = postRepository.findByIsApprovedTrueAndTitleContainingIgnoreCase(keyword, pageable);
        return convertToPageResult(posts, userId);
    }

    public PageResult<PostDTO.GetResponse> getPinnedPosts(Pageable pageable, Long userId) {
        Page<Post> posts = postRepository.findByIsApprovedTrueAndIsPinnedTrueOrderByCreatedAtDesc(pageable);
        return convertToPageResult(posts, userId);
    }

    public PageResult<PostDTO.GetResponse> getPopularPosts(Pageable pageable, Long userId) {
        Page<Post> posts = postRepository.findByIsApprovedTrueOrderByViewCountDesc(pageable);
        return convertToPageResult(posts, userId);
    }

    public PageResult<PostDTO.GetResponse> getMostPurchasedPosts(Pageable pageable, Long userId) {
        Page<Post> posts = postRepository.findTopPurchasedPosts(pageable);
        return convertToPageResult(posts, userId);
    }

    public PostDTO.GetResponse getPostById(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        // 增加浏览量
        post.incrementViewCount();
        postRepository.save(post);

        // 创建用户活动记录
        // createUserActivity(userId, UserActivity.ActivityType.VIEW_POST);

        return convertToGetResponse(post, userId);
    }

    public PostDTO.GetResponse updatePost(Long postId, Long userId, PostDTO.UpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        // 检查权限
        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("无权限修改此帖子");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setPrice(request.getPrice());
        post.setIsPinned(request.getPinned() != null ? request.getPinned() : post.getIsPinned());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return convertToGetResponse(savedPost, userId);
    }

    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        // 检查权限
        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("无权限删除此帖子");
        }

        postRepository.delete(post);
    }

    public PostDTO.LikeResponse toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        // 检查用户是否已经点赞
        // 这里简化处理，实际应该有单独的点赞表
        // boolean isLiked = checkIfUserLikedPost(postId, userId);

        // 创建用户活动记录
        // createUserActivity(userId, UserActivity.ActivityType.LIKE_POST);

        if (/*isLiked*/false) {
            // 取消点赞
            post.incrementLikeCount();
        } else {
            // 点赞
            post.incrementLikeCount();
        }

        Post savedPost = postRepository.save(post);
        return convertToLikeResponse(savedPost);
    }

    public PostDTO.PurchaseResponse purchasePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 检查是否已经购买
        if (purchaseRepository.existsByBuyerIdAndPostId(userId, postId)) {
            throw new RuntimeException("已经购买过此帖子");
        }

        // 检查学习币余额
        if (buyer.getLearningCoins() < post.getPrice().longValue()) {
            throw new RuntimeException("学习币余额不足");
        }

        // 扣除学习币
        buyer.deductLearningCoins(post.getPrice().longValue());
        userRepository.save(buyer);

        // 增加作者学习币
        User author = post.getAuthor();
        author.addLearningCoins(post.getPrice().longValue());
        userRepository.save(author);

        // 创建购买记录
        PostPurchase purchase = new PostPurchase();
        purchase.setBuyer(buyer);
        purchase.setPost(post);
        purchase.setPurchasePrice(post.getPrice());
        purchaseRepository.save(purchase);

        // 更新帖子购买数量
        post.incrementPurchaseCount();
        postRepository.save(post);

        // 创建用户活动记录
        // createUserActivity(userId, UserActivity.ActivityType.PURCHASE_POST);

        return convertToPurchaseResponse(post);
    }

    public List<PostDTO.GetResponse> getPurchasedPosts(Long userId) {
        List<Post> purchasedPosts = purchaseRepository.findPurchasedPostsByUserId(userId);
        return purchasedPosts.stream()
                .map(post -> convertToGetResponse(post, userId))
                .toList();
    }

    private PostDTO.CreateResponse convertToCreateResponse(Post post) {
        PostDTO.CreateResponse response = new PostDTO.CreateResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setPrice(post.getPrice());
        response.setIsPinned(post.getIsPinned());
        response.setCreatedAt(post.getCreatedAt());
        response.setAuthorId(post.getAuthor().getId());
        response.setAuthorName(post.getAuthor().getUsername());
        return response;
    }

    private PostDTO.GetResponse convertToGetResponse(Post post, Long userId) {
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
        response.setPurchasedByCurrentUser(isPurchasedByUser(userId, post.getId()));
        return response;
    }

    private PostDTO.LikeResponse convertToLikeResponse(Post post) {
        PostDTO.LikeResponse response = new PostDTO.LikeResponse();
        response.setId(post.getId());
        response.setLikeCount(post.getLikeCount());
        return response;
    }

    private PostDTO.PurchaseResponse convertToPurchaseResponse(Post post) {
        PostDTO.PurchaseResponse response = new PostDTO.PurchaseResponse();
        response.setId(post.getId());
        response.setPrice(post.getPrice());
        response.setPurchaseCount(post.getPurchaseCount());
        return response;
    }

    private PageResult<PostDTO.GetResponse> convertToPageResult(Page<Post> posts, Long userId) {
        List<PostDTO.GetResponse> responseList = posts.getContent().stream()
                .map(post -> convertToGetResponse(post, userId))
                .toList();

        return PageResult.of(responseList, posts.getTotalElements(), posts.getNumber() + 1, posts.getSize());
    }

    private boolean isPurchasedByUser(Long userId, Long postId) {
        return purchaseRepository.existsByBuyerIdAndPostId(userId, postId);
    }

    // DTO内部类
    public static class CreateResponse extends PostDTO {
        private Long authorId;
        private String authorName;
        private LocalDateTime createdAt;
        // Getters and Setters
        public Long getAuthorId() { return authorId; }
        public void setAuthorId(Long authorId) { this.authorId = authorId; }
        public String getAuthorName() { return authorName; }
        public void setAuthorName(String authorName) { this.authorName = authorName; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class GetResponse extends PostDTO {
        private Long authorId;
        private String authorName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean purchasedByCurrentUser;
        // Getters and Setters
        public Long getAuthorId() { return authorId; }
        public void setAuthorId(Long authorId) { this.authorId = authorId; }
        public String getAuthorName() { return authorName; }
        public void setAuthorName(String authorName) { this.authorName = authorName; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public Boolean getPurchasedByCurrentUser() { return purchasedByCurrentUser; }
        public void setPurchasedByCurrentUser(Boolean purchasedByCurrentUser) { this.purchasedByCurrentUser = purchasedByCurrentUser; }
    }

    public static class LikeResponse extends PostDTO {
        private Long likeCount;
        // Getters and Setters
        public Long getLikeCount() { return likeCount; }
        public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    }

    public static class PurchaseResponse extends PostDTO {
        private Long purchaseCount;
        // Getters and Setters
        public Long getPurchaseCount() { return purchaseCount; }
        public void setPurchaseCount(Long purchaseCount) { this.purchaseCount = purchaseCount; }
    }
}