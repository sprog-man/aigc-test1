package com.aiteam.repository;

import com.aiteam.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    Page<Comment> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);
    Page<Comment> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.parent IS NULL ORDER BY c.createdAt DESC")
    Page<Comment> findTopLevelComments(Pageable pageable);
}