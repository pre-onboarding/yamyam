package com.wanted.yamyam.domain.review.repo;

import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.review.entity.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
    long countByStoreId(Long storeId);

    List<Review> findByStoreId(Long storeId);
}
