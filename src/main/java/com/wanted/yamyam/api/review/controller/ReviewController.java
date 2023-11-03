package com.wanted.yamyam.api.review.controller;

import com.wanted.yamyam.api.review.dto.ReviewRequest;
import com.wanted.yamyam.api.review.dto.ReviewResponse;
import com.wanted.yamyam.api.review.service.ReviewService;
import com.wanted.yamyam.api.store.service.StoreService;
import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.store.entity.Store;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * 맛집 평가 정보관련 엔드포인트를 지정하는 Controller 입니다.
 * @author 정성국
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class ReviewController {

    private final ReviewService reviewService;
    private final StoreService storeService;

    /**
     * 사용자로부터 맛집 평가 점수 및 내용을 받아 저장한 후 저장된 맛집 평가 정보를 반환합니다.
     * @param storeId 맛집의 식별자
     * @param requestBody 맛집 평가 점수 및 내용
     * @return 저장된 맛집 평가 정보를 반환합니다.
     * @author 정성국
     */
    @PostMapping("stores/{storeId}/reviews")
    public ResponseEntity<ReviewResponse> saveReview(@PathVariable long storeId,
                                                     @Valid @RequestBody ReviewRequest requestBody) {

        // TODO: Member 구현 후 사용자별 memberId 대입 되도록 수정 요함
        Review review = mapReviewRequestDtoToReviewEntity(requestBody, storeId, 1L);
        Review savedReview = reviewService.saveReview(review);
        storeService.updateRating(savedReview);
        String locationUrl = "/api/v1/stores/%d/reviews".formatted(storeId);
        return ResponseEntity.created(URI.create(locationUrl)).body(mapReviewEntityToReviewResponseDto(savedReview));
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
