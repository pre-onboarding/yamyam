package com.wanted.yamyam.api.store.service;

import com.wanted.yamyam.api.store.dto.StoreDetailResponse;
import com.wanted.yamyam.api.store.dto.StoreResponse;
import com.wanted.yamyam.api.store.dto.StoreListResponse;
import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.entity.StoreId;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import com.wanted.yamyam.global.exception.ErrorCode;
import com.wanted.yamyam.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import static com.wanted.yamyam.global.exception.ErrorCode.LAT_LON_NO_VALUE;
import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.review.repo.ReviewRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;
  
    private final ReviewRepository reviewRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY = "storesDetail";

    /**
     * 맛집 목록 조회
     * @param lat : 원하는 위치의 위도
     * @param lon : 원하는 위치의 경도
     * @param range : 원하는 반경 조회 km
     * @return
     */
    @Transactional(readOnly = true)
    public StoreListResponse storeList(Pageable pageable, String lat, String lon, double range) {
        if (lat.isEmpty() || lon.isEmpty()) {
            throw new ErrorException(LAT_LON_NO_VALUE);
        }

        /**
         * 1km에 위도 경도 차이(대략) : 위도 = 0.009009, 경도 = 0.011236
         * lat - (0.009009 * range) ~ lat + (0.009009 * range)
         * lon - (0.011236 * range) ~ lon + (0.011236 * range)
         * 파라미터로 넘어온 lat, lon으로 위 계산으로 범위를 구하면 대략 range 반경에 있는 맛집을 조회할 수 있다.
         */
        double leftLat = Double.parseDouble(lat) - (0.009009 * range);
        double rightLat = Double.parseDouble(lat) + (0.009009 * range);
        double leftLon = Double.parseDouble(lon) - (0.011236 * range);
        double rightLon = Double.parseDouble(lon) + (0.011236 * range);


        /** 1. range 반경에 있는 맛집 목록을 StoreResponse에 맞게 불러온다. */
        Page<StoreResponse> list = storeRepository.findByStoreList(pageable, Double.parseDouble(lat), Double.parseDouble(lon), leftLat, leftLon, rightLat, rightLon);

        return new StoreListResponse(list.get().collect(Collectors.toList()), list.getTotalPages());
    }

    /**
     * 맛집 평가가 추가된 경우 해당 맛집 평가를 받아 전체 맛집 평점을 계산하여 갱신 후 갱신된 평점을 반환합니다.
     * @param review 새로운 맛집 평가
     * @return 갱신된 맛집 평점
     */
    @Transactional
    public double updateRating(Review review) {
        var storeId = new StoreId(review.getStore().getName(), review.getStore().getAddress());
        long oldRatingTotalCount = reviewRepository.countByStoreNameAndStoreAddress(storeId.getName(), storeId.getAddress()) - 1;
        double oldRating = review.getStore().getRating();
        double newRating = (oldRating * oldRatingTotalCount + review.getScore()) / (oldRatingTotalCount + 1);
        storeRepository.updateRatingById(storeId.getName(), storeId.getAddress(), newRating);
        return newRating;
    }

    /**
     * 맛집 상세 정보
     * storeId로 맛집을 상세 정보를 조회한다. 만약 존재하지 않는 맛집이면 예외처리.
     * storeId로 조회한 맛집에 리뷰 리스트를 조회한다.
     * 조회한 맛집 상세 정보, 리뷰 리스트를 반환한다.
     * @param storeId
     * @return
     */
    @Transactional(readOnly = true)
    public StoreDetailResponse storeDetail(String storeId) {
        Store store = getStoreDetailFromRedis(storeId);

        if (store == null) {
            String[] split = storeId.split(":");
            StoreId id = new StoreId(split[0], split[1]);
            store = storeRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.NON_EXISTENT_STORE));
            saveStoreDetailToRedis(store, storeId);
        }

        StoreDetailResponse response = new StoreDetailResponse(store);

        return response;
    }

    private void saveStoreDetailToRedis(Store store, String storeId) {
        redisTemplate.opsForValue().set(KEY + " " + storeId, store, Duration.ofMinutes(10));
    }

    private Store getStoreDetailFromRedis(String storeId) {
        return (Store) redisTemplate.opsForValue().get(KEY + " " + storeId);
    }
}
