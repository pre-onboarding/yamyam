package com.wanted.yamyam.api.store.service;

import com.wanted.yamyam.api.store.dto.StoreDetailResponse;
import com.wanted.yamyam.api.store.dto.StoreResponse;
import com.wanted.yamyam.api.store.dto.StoreListResponse;
import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import com.wanted.yamyam.global.exception.ErrorCode;
import com.wanted.yamyam.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public StoreListResponse storeList(String sort, int page, int pageCount, String lat, String lon, double range) {
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
        List<StoreResponse> list = storeRepository.findByAllList(leftLat, leftLon, rightLat, rightLon);

        /** 2. 조회한 맛집 목록을 Loop 돌면서 파라미터로 넘어온 위치에서 range 반경에 있는 맛집들과의 거리 차이를 구해서 StoreResponse.distance에 저장한다. */
        for (int i = 0; i < list.size(); i++) {
            StoreResponse store = list.get(i);
            double distance = findDistance(Double.parseDouble(lat), Double.parseDouble(lon), store.getLat(), store.getLon());
            log.info("distance = {}", distance); // 잠시 거리 차이 확인용 추후 삭제
            store.setDistance(distance);
        }

        /**
         * 3. StoreResponse에 저장된 맛집 목록을 거리순 or 평점순으로 정렬한다.(default = 거리순)
         * (StoreResponse에 거리차이를 저장 해야 거리순 정렬이 가능해서 직접 정렬했음.)
         */
        if (sort.equals("rating")) {
            list = list.stream().sorted(Comparator.comparing(StoreResponse::getRating).reversed()).collect(Collectors.toList());
        } else {
            list = list.stream().sorted(Comparator.comparing(StoreResponse::getDistance)).collect(Collectors.toList());
        }

        /**
         * 4. StoreResponse를 파라미터로 들어온 page, pageCount에 맞게 return 하기 위한 페이징처리
         * (3번 작업(정렬) 후 페이지를 나눠야하기 때문에 직접 페이징 처리 했음.)
         */
        int[] paging = storeResponsePaging(page, pageCount, list.size());

        return new StoreListResponse(list.subList(paging[0], paging[1]), paging[2]);
    }

    /** StoreResponse를 직접 페이징 처리하기 위한 메서드 */
    private int[] storeResponsePaging(int page, int pageCount, int total) {
        int totalPage;
        if (total % pageCount == 0) {
            totalPage = total / pageCount;
        } else {
            totalPage = total / pageCount + 1;
        }

        int fromIndex = page * pageCount;
        if (fromIndex < 0) {
            fromIndex = 0;
        } else if (fromIndex > total) {
            fromIndex = total;
        }

        int toIndex = fromIndex + pageCount;
        if (toIndex > total) {
            toIndex = total;
        }

        return new int[]{fromIndex, toIndex, totalPage};
    }

    /**
     * 맛집 목록에 있는 위도, 경도와 입력값으로 들어온 위도, 경도의 거리를 구합니다.
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    private double findDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        double R = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * 맛집 평가가 추가된 경우 해당 맛집 평가를 받아 전체 맛집 평점을 계산하여 갱신 후 갱신된 평점을 반환합니다.
     * @param review 새로운 맛집 평가
     * @return 갱신된 맛집 평점
     */
    @Transactional
    public double updateRating(Review review) {
        long oldRatingTotalCount = reviewRepository.countByStoreId(review.getStore().getId()) - 1;
        double oldRating = review.getStore().getRating();
        double newRating = (oldRating * oldRatingTotalCount + review.getScore()) / (oldRatingTotalCount + 1);
        storeRepository.updateRatingById(review.getStore().getId(), newRating);
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
    public StoreDetailResponse storeDetail(Long storeId) {
        Store store = getStoreDetailFromRedis(storeId);

        if (store == null) {
            store = storeRepository.findById(storeId).orElseThrow(() -> new ErrorException(ErrorCode.NON_EXISTENT_STORE));
            saveStoreDetailToRedis(store);
        }

        StoreDetailResponse response = new StoreDetailResponse(store);

        return response;
    }

    private void saveStoreDetailToRedis(Store store) {
        redisTemplate.opsForValue().set(KEY + " " + store.getId(), store, Duration.ofMinutes(10));
    }

    private Store getStoreDetailFromRedis(Long storeId) {
        return (Store) redisTemplate.opsForValue().get(KEY + " " + storeId);
    }
}
