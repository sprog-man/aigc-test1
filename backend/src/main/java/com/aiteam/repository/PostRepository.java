package com.aiteam.repository;

import com.aiteam.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByIsApprovedTrueOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findByIsApprovedTrueAndTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Post> findByIsApprovedTrueAndAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);
    Page<Post> findByIsApprovedTrueAndIsPinnedTrueOrderByCreatedAtDesc(Pageable pageable);
    List<Post> findByIsApprovedTrueOrderByViewCountDesc(Pageable pageable);
    List<Post> findByIsApprovedTrueOrderByLikeCountDesc(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isApproved = true ORDER BY p.purchaseCount DESC")
    List<Post> findTopPurchasedPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isApproved = true AND p.price > 0")
    List<Post> findPaidPosts(Pageable pageable);
}