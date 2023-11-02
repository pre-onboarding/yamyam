package com.wanted.yamyam.api.store.service;

import com.wanted.yamyam.api.store.dto.StoreResponse;
import com.wanted.yamyam.api.store.dto.StoreListResponse;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import com.wanted.yamyam.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.wanted.yamyam.global.exception.ErrorCode.LAT_LON_NO_VALUE;

@RequiredArgsConstructor
@Service
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     * 맛집 목록 조회
     * @param pageable
     * @param lat : 원하는 위치의 위도
     * @param lon : 원하는 위치의 경도
     * @param range : 원하는 반경 조회 km
     * @return
     */
    @Transactional(readOnly = true)
    public StoreListResponse storeList(Pageable pageable, String lat, String lon, Double range) {
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

        /** 2. 조회한 맛집 목록을 Loop 돌면서 파라미터로 넘어온 위치에서 range 반경에 있는 맛집들과의 거리 차이를 구해서 dto.distance에 저장한다. */
        for (int i = 0; i < list.size(); i++) {
            StoreResponse store = list.get(i);
            double distance = findDistance(Double.parseDouble(lat), Double.parseDouble(lon), store.getLat(), store.getLon());
            log.info("distance = {}", distance); // 잠시 거리 차이 확인용 추후 삭제
            store.setDistance(distance);
        }

        /** 3. dto에 저장된 맛집 목록을 거리순 or 평점순으로 정렬한다. */
        /** todo: 3번 항목 */

        /** todo: list.size() 추후 pageCount로 변경 */
        StoreListResponse storeListResponse = new StoreListResponse(list, list.size());

        return storeListResponse;
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
}
