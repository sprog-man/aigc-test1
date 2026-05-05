package com.aiteam.controller;

import com.aiteam.dto.CommentDTO;
import com.aiteam.dto.PageResult;
import com.aiteam.dto.Response;
import com.aiteam.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Response<CommentDTO.CreateResponse>> createComment(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CommentDTO.CreateRequest request) {
        try {
            Long userId = extractUserIdFromToken(token);
            CommentDTO.CreateResponse response = commentService.createComment(userId, request);
            return ResponseEntity.ok(Response.success(response, "评论创建成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<Response<CommentDTO.CreateResponse>> createCommentWithPostId(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CommentDTO.CreateRequest request) {
        try {
            Long userId = extractUserIdFromToken(token);
            CommentDTO.CreateResponse response = commentService.createCommentWithPostId(userId, postId, request);
            return ResponseEntity.ok(Response.success(response, "评论创建成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Response<PageResult<CommentDTO.GetResponse>>> getPostComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PageResult<CommentDTO.GetResponse> result = commentService.getPostComments(postId, pageable);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response<PageResult<CommentDTO.GetResponse>>> getUserComments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PageResult<CommentDTO.GetResponse> result = commentService.getUserComments(userId, pageable);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/replies/{parentId}")
    public ResponseEntity<Response<PageResult<CommentDTO.GetResponse>>> getReplies(
            @PathVariable Long parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PageResult<CommentDTO.GetResponse> result = commentService.getReplies(parentId, pageable);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/top-level")
    public ResponseEntity<Response<PageResult<CommentDTO.GetResponse>>> getTopLevelComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PageResult<CommentDTO.GetResponse> result = commentService.getTopLevelComments(pageable);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Response<CommentDTO.GetResponse>> updateComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CommentDTO.UpdateRequest request) {
        try {
            Long userId = extractUserIdFromToken(token);
            CommentDTO.GetResponse response = commentService.updateComment(commentId, userId, request);
            return ResponseEntity.ok(Response.success(response, "更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Response<String>> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            commentService.deleteComment(commentId, userId);
            return ResponseEntity.ok(Response.success("删除成功"));
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
}