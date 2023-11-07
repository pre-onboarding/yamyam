package com.wanted.yamyam.api.store.service;

import com.wanted.yamyam.api.store.dto.StoreDetailResponse;
import com.wanted.yamyam.api.store.dto.StoreListResponse;
import com.wanted.yamyam.api.review.dto.ReviewRequest;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;
  
    @Mock
    ReviewRepository reviewRepository;

    @Mock
    private RedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Test
    @DisplayName("맛집 목록 조회")
    @Transactional(readOnly = true)
    void storeList() {
        // given
        String sortByDistance = "distance";
        String sortByRating = "rating";
        int page = 0;
        int pageCount = 10;
        String lat = "38.0362201660";
        String lon = "127.3675276602";
        double range = 100.0;

        // when
        StoreListResponse response = storeService.storeList(sortByDistance, page, pageCount, lat, lon, range);
        StoreListResponse response2 = storeService.storeList(sortByRating, page, pageCount, lat, lon, range);

        // then
        assertThat(response.getStores()).isNotNull();
        assertThat(response2.getStores()).isNotNull();
    }
  
    @Test
    @DisplayName("신규 맛집 평가 점수를 반영하여 맛집 평점 업데이트")
    void shouldUpdateStoreRating() {

        var testScore = 5;
        var testContent = "맛있어요!!";
        var testUsername = "식신";
        var oldRating = 2.5;
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

    @DisplayName("맛집 상세 정보 조회")
    @Test
    @Transactional
    void storeDetail() {
        // given
        Store store = new Store("카페밀", "경기도 오산시 밀머리로 48 (원동)", 37.1375168981, 127.0756273255,  "중국식", 3.0);
        String storeId = "카페밀:경기도 오산시 밀머리로 48 (원동)";

        // stub
        when(storeRepository.findById(any())).thenReturn(Optional.of(store));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        // when
        StoreDetailResponse response = storeService.storeDetail(storeId);

        // then
        assertAll(
                () -> assertThat(response.getLat()).isEqualTo(store.getLat()),
                () -> assertThat(response.getLon()).isEqualTo(store.getLon()),
                () -> assertThat(response.getName()).isEqualTo(store.getName()),
                () -> assertThat(response.getAddress()).isEqualTo(store.getAddress()),
                () -> assertThat(response.getCategory()).isEqualTo(store.getCategory()),
                () -> assertThat(response.getRating()).isEqualTo(store.getRating())
        );

    }
}

