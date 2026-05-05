package com.aiteam.repository;

import com.aiteam.entity.PostPurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPurchaseRepository extends JpaRepository<PostPurchase, Long> {
    List<PostPurchase> findByBuyerId(Long buyerId);
    List<PostPurchase> findByPostId(Long postId);
    boolean existsByBuyerIdAndPostId(Long buyerId, Long postId);

    @Query("SELECT p FROM Post p JOIN p.purchases pu WHERE pu.buyer.id = :buyerId")
    List<Post> findPurchasedPostsByUserId(Long buyerId, Pageable pageable);

    @Query("SELECT pu.post FROM PostPurchase pu WHERE pu.buyer.id = :buyerId GROUP BY pu.post ORDER BY COUNT(pu.post) DESC")
    List<Post> findMostPurchasedPostsByUser(Long buyerId, Pageable pageable);
}