package com.wanted.yamyam.api.store.service;

import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.review.repo.ReviewRepository;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 맛집과 관련된 로직을 수행합니다.
 * @author 정성국
 */
@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 맛집 평가가 추가된 경우 해당 맛집 평가를 받아 전체 맛집 평점을 계산하여 갱신 후 갱신된 평점을 반환합니다.
     * @param review 새로운 맛집 평가
     * @return 갱신된 맛집 평점
     */
    @Transactional
    public String updateRating(Review review) {
        long oldRatingTotalCount = reviewRepository.countByStoreId(review.getStore().getId()) - 1;
        String oldRating = review.getStore().getRating();
        double newRatingDouble = (Double.parseDouble(oldRating) * oldRatingTotalCount + review.getScore()) / (oldRatingTotalCount + 1);
        BigDecimal newRating = new BigDecimal(newRatingDouble).setScale(1, RoundingMode.HALF_UP);
        storeRepository.updateRatingById(review.getStore().getId(), newRating.toString());
        return newRating.toString();
    }
}
