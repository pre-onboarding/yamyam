package com.wanted.yamyam.api.review.service;

import com.wanted.yamyam.api.review.dto.StoreByReviewListResponse;
import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.review.repo.ReviewRepository;
import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @DisplayName("맛집 리뷰 리스트")
    @Test
    @Transactional(readOnly = true)
    void reviewList() {
        // given
        Store store = new Store(1L, 37.1375168981, 127.0756273255, "카페밀", "경기도 오산시 밀머리로 48 (원동)", "중국식", 3.0);
        Long storeId = 1L;
        Member member = new Member(1L, "email@gmail.com", "askjdlk1", null, null);
        Review review = new Review(member, store, 3, "맛있어요~");
        List<Review> list = new ArrayList<>();
        list.add(review);

        // stub
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(reviewRepository.findByStoreId(storeId)).thenReturn(list);

        // when
        List<StoreByReviewListResponse> responses = reviewService.reviewList(storeId);

        // then
        assertAll(
                () -> assertThat(responses.get(0).getMemberId()).isEqualTo(review.getMember().getId()),
                () -> assertThat(responses.get(0).getScore()).isEqualTo(review.getScore()),
                () -> assertThat(responses.get(0).getContent()).isEqualTo(review.getContent())
        );

    }

}