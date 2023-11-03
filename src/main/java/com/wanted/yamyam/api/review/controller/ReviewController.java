package com.wanted.yamyam.api.review.controller;

import com.wanted.yamyam.api.review.dto.ReviewRequest;
import com.wanted.yamyam.api.review.dto.ReviewResponse;
import com.wanted.yamyam.api.review.service.ReviewService;
import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.store.entity.Store;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("stores/{storeId}/reviews")
    public ResponseEntity<ReviewResponse> saveReview(@PathVariable long storeId,
                                                     @Valid @RequestBody ReviewRequest requestBody) {

        // TODO: Member 구현 후 사용자별 memberId 대입 되도록 수정 요함
        Review review = mapReviewRequestDtoToReviewEntity(requestBody, storeId, 1L);
        review = reviewService.saveReview(review);
        String locationUrl = "/api/v1/stores/%d/reviews".formatted(storeId);
        return ResponseEntity.created(URI.create(locationUrl)).body(mapReviewEntityToReviewResponseDto(review));
    }

    private Review mapReviewRequestDtoToReviewEntity(ReviewRequest reviewRequest, long storeId, long memberId) {
        return Review.builder()
                .store(Store.builder().id(storeId).build())
                .member(Member.builder().id(memberId).build())
                .score(reviewRequest.score())
                .content(reviewRequest.content())
                .build();
    }

    private ReviewResponse mapReviewEntityToReviewResponseDto(Review review) {
        return new ReviewResponse(
                review.getMember().getId(),
                review.getStore().getId(),
                review.getMember().getUsername(),
                review.getScore(),
                review.getContent());
    }

}
