package com.wanted.yamyam.api.review.service;

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
        if (reviewRepository.findById(new ReviewId(review.getMember(), review.getStore())).isPresent()) {
            throw new ErrorException(ErrorCode.DUPLICATE_REVIEW);
        }
        // TODO: member와 store에 id만 지정해서 저장하는 경우 Detached Entity Passed to Persist 가 발생하였습니다.
        //  해결 방법을 찾을 때까지 임시로 각각의 데이터를 불러와서 지정하도록 하겠습니다.
        var test = Review.builder().member(memberRepository.findById(review.getMember().getId()).get())
                .store(storeRepository.findById(review.getStore().getId()).get())
                .score(review.getScore())
                .content(review.getContent()).build();
        return reviewRepository.save(test);
    }

}
