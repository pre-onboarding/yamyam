package com.wanted.yamyam.api.review.service;

import com.wanted.yamyam.api.review.dto.StoreByReviewListResponse;
import com.wanted.yamyam.domain.member.repo.MemberRepository;
import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.review.entity.ReviewId;
import com.wanted.yamyam.domain.review.repo.ReviewRepository;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import com.wanted.yamyam.global.exception.ErrorCode;
import com.wanted.yamyam.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    /**
     * 사용자의 신규 맛집 평가를 받아 저장합니다.
     * @param review 사용자가 신규 작성한 맛집 평가
     * @return 저장된 사용자의 맛집 평가를 반환합니다.
     * @author 정성국
     */
    @Transactional
    public Review saveReview(Review review) {
        validateData(review);
        var reviewEntity = Review.builder()
                .member(memberRepository.getReferenceById(review.getMember().getId()))
                .store(storeRepository.getReferenceById(review.getStore().getId()))
                .score(review.getScore())
                .content(review.getContent()).build();
        return reviewRepository.save(reviewEntity);
    }

    private void validateData(Review review) {
        // 존재하지 않는 맛집인 경우
        if (!storeRepository.existsById(review.getStore().getId()))
            throw new ErrorException(ErrorCode.NON_EXISTENT_STORE);

        // 이미 작성한 리뷰가 존재하는 경우
        if (reviewRepository.existsById(new ReviewId(review.getMember(), review.getStore())))
            throw new ErrorException(ErrorCode.DUPLICATE_REVIEW);
    }

    /**
     * storeId로 맛집에 리뷰 리스트를 조회해서 리스트를 반환한다.
     * @param storeId
     * @return
     */
    @Transactional(readOnly = true)
    public List<StoreByReviewListResponse> reviewList(Long storeId) {
        storeRepository.findById(storeId).orElseThrow(() -> new ErrorException(ErrorCode.NON_EXISTENT_STORE));
        List<StoreByReviewListResponse> responses = reviewRepository.findByStoreId(storeId).stream().map(StoreByReviewListResponse::new).collect(Collectors.toList());

        return responses;
    }

}
