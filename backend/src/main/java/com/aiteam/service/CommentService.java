package com.aiteam.service;

import com.aiteam.dto.CommentDTO;
import com.aiteam.entity.Comment;
import com.aiteam.entity.User;
import com.aiteam.entity.Post;
import com.aiteam.repository.CommentRepository;
import com.aiteam.repository.UserRepository;
import com.aiteam.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentDTO.CreateResponse createComment(Long userId, CommentDTO.CreateRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(author);

        // 如果有父评论，设置关联
        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("父评论不存在"));
            comment.setParent(parent);
        }

        // 需要传入postId，这里假设request中有postId
        // 如果没有，需要修改请求类或从其他地方获取
        // comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        // 创建用户活动记录
        // createUserActivity(userId, UserActivity.ActivityType.COMMENT_POST);

        return convertToCreateResponse(savedComment);
    }

    public PageResult<CommentDTO.GetResponse> getPostComments(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
        return convertToPageResult(comments);
    }

    public PageResult<CommentDTO.GetResponse> getUserComments(Long userId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByAuthorIdOrderByCreatedAtDesc(userId, pageable);
        return convertToPageResult(comments);
    }

    public PageResult<CommentDTO.GetResponse> getReplies(Long parentId, Pageable pageable) {
        Page<Comment> replies = commentRepository.findByParentIdOrderByCreatedAtDesc(parentId, pageable);
        return convertToPageResult(replies);
    }

    public CommentDTO.GetResponse updateComment(Long commentId, Long userId, CommentDTO.UpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        // 检查权限
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("无权限修改此评论");
        }

        comment.setContent(request.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return convertToGetResponse(savedComment);
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        // 检查权限
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("无权限删除此评论");
        }

        commentRepository.delete(comment);
    }

    private CommentDTO.CreateResponse convertToCreateResponse(Comment comment) {
        CommentDTO.CreateResponse response = new CommentDTO.CreateResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        response.setAuthorId(comment.getAuthor().getId());
        response.setAuthorName(comment.getAuthor().getUsername());
        response.setPostId(comment.getPost().getId());
        response.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        return response;
    }

    private CommentDTO.GetResponse convertToGetResponse(Comment comment) {
        CommentDTO.GetResponse response = new CommentDTO.GetResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        response.setAuthorId(comment.getAuthor().getId());
        response.setAuthorName(comment.getAuthor().getUsername());
        response.setPostId(comment.getPost().getId());
        response.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        return response;
    }

    private PageResult<CommentDTO.GetResponse> convertToPageResult(Page<Comment> comments) {
        List<CommentDTO.GetResponse> responseList = comments.getContent().stream()
                .map(this::convertToGetResponse)
                .toList();

        return PageResult.of(responseList, comments.getTotalElements(), comments.getNumber() + 1, comments.getSize());
    }

    // 需要修改CommentDTO.CreateRequest以包含postId
    // 或者在createComment方法中通过其他方式获取postId
    public CommentDTO.CreateResponse createCommentWithPostId(Long userId, Long postId, CommentDTO.CreateRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(author);
        comment.setPost(post);

        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("父评论不存在"));
            comment.setParent(parent);
        }

        Comment savedComment = commentRepository.save(comment);

        return convertToCreateResponse(savedComment);
    }

    // DTO内部类
    public static class CreateResponse extends CommentDTO {
        private Long authorId;
        private String authorName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long postId;
        private Long parentId;
        // Getters and Setters
        public Long getAuthorId() { return authorId; }
        public void setAuthorId(Long authorId) { this.authorId = authorId; }
        public String getAuthorName() { return authorName; }
        public void setAuthorName(String authorName) { this.authorName = authorName; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public Long getPostId() { return postId; }
        public void setPostId(Long postId) { this.postId = postId; }
        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
    }

    public static class GetResponse extends CommentDTO {
        private Long authorId;
        private String authorName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long postId;
        private Long parentId;
        // Getters and Setters
        public Long getAuthorId() { return authorId; }
        public void setAuthorId(Long authorId) { this.authorId = authorId; }
        public String getAuthorName() { return authorName; }
        public void setAuthorName(String authorName) { this.authorName = authorName; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public Long getPostId() { return postId; }
        public void setPostId(Long postId) { this.postId = postId; }
        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
    }
}