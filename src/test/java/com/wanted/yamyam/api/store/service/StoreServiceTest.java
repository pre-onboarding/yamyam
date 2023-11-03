package com.wanted.yamyam.api.store.service;

import com.wanted.yamyam.api.review.dto.ReviewRequest;
import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.review.repo.ReviewRepository;
import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    StoreRepository storeRepository;

    @InjectMocks
    StoreService storeService;

    @Test
    @DisplayName("신규 맛집 평가 점수를 반영하여 맛집 평점 업데이트")
    void shouldUpdateStoreRating() {

        var testScore = 5;
        var testContent = "맛있어요!!";
        var testUsername = "식신";
        var oldRating = "2.5";
        var oldRatingTotalCount = 30L;
        var testMember = Member.builder().id(1L).username(testUsername).build();
        var testStore = Store.builder().id(1L).rating(oldRating).build();
        var testReviewRequest = new ReviewRequest(testScore, testContent);
        var testReview = Review.builder()
                .member(testMember)
                .store(testStore)
                .score(testReviewRequest.score())
                .content(testReviewRequest.content())
                .build();

        // 새로운 review를 저장한 후에 count를 수행하므로, 기존 count 값에 + 1을 해줍니다.
        when(reviewRepository.countByStoreId(testReview.getStore().getId())).thenReturn(oldRatingTotalCount + 1);

        var newRating = "2.6";

        assertThat(storeService.updateRating(testReview)).isEqualTo(newRating);

    }

}
