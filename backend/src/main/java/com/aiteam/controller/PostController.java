package com.aiteam.controller;

import com.aiteam.dto.PageResult;
import com.aiteam.dto.PostDTO;
import com.aiteam.dto.Response;
import com.aiteam.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Response<PostDTO.CreateResponse>> createPost(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody PostDTO.CreateRequest request) {
        try {
            Long userId = extractUserIdFromToken(token);
            PostDTO.CreateResponse response = postService.createPost(userId, request);
            return ResponseEntity.ok(Response.success(response, "帖子创建成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Response<PageResult<PostDTO.GetResponse>>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PageResult<PostDTO.GetResponse> result = postService.getPublicPosts(pageable, null);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/my-posts")
    public ResponseEntity<Response<PageResult<PostDTO.GetResponse>>> getMyPosts(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Long userId = extractUserIdFromToken(token);
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PageResult<PostDTO.GetResponse> result = postService.getPostsByAuthor(userId, pageable, userId);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Response<PostDTO.GetResponse>> getPost(
            @PathVariable Long postId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long userId = token != null ? extractUserIdFromToken(token) : null;
            PostDTO.GetResponse response = postService.getPostById(postId, userId);
            return ResponseEntity.ok(Response.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Response<PageResult<PostDTO.GetResponse>>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PageResult<PostDTO.GetResponse> result = postService.getSearchPosts(keyword, pageable, null);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/pinned")
    public ResponseEntity<Response<PageResult<PostDTO.GetResponse>>> getPinnedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PageResult<PostDTO.GetResponse> result = postService.getPinnedPosts(pageable, null);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<Response<PageResult<PostDTO.GetResponse>>> getPopularPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "viewCount"));

            PageResult<PostDTO.GetResponse> result = postService.getPopularPosts(pageable, null);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/most-purchased")
    public ResponseEntity<Response<PageResult<PostDTO.GetResponse>>> getMostPurchasedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "purchaseCount"));

            PageResult<PostDTO.GetResponse> result = postService.getMostPurchasedPosts(pageable, null);
            return ResponseEntity.ok(Response.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Response<PostDTO.GetResponse>> updatePost(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody PostDTO.UpdateRequest request) {
        try {
            Long userId = extractUserIdFromToken(token);
            PostDTO.GetResponse response = postService.updatePost(postId, userId, request);
            return ResponseEntity.ok(Response.success(response, "更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Response<String>> deletePost(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            postService.deletePost(postId, userId);
            return ResponseEntity.ok(Response.success("删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Response<PostDTO.LikeResponse>> toggleLike(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            PostDTO.LikeResponse response = postService.toggleLike(postId, userId);
            return ResponseEntity.ok(Response.success(response, "操作成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @PostMapping("/{postId}/purchase")
    public ResponseEntity<Response<PostDTO.PurchaseResponse>> purchasePost(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            PostDTO.PurchaseResponse response = postService.purchasePost(postId, userId);
            return ResponseEntity.ok(Response.success(response, "购买成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Response.error(e.getMessage()));
        }
    }

    @GetMapping("/my-purchases")
    public ResponseEntity<Response<List<PostDTO.GetResponse>>> getMyPurchases(
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            List<PostDTO.GetResponse> response = postService.getPurchasedPosts(userId);
            return ResponseEntity.ok(Response.success(response));
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