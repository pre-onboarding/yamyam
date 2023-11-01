package com.wanted.yamyam.domain.review.repo;

import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.review.entity.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {

}
