package com.wanted.yamyam.api.store;

import com.wanted.yamyam.api.store.dto.StoreList;
import com.wanted.yamyam.api.store.dto.StoreListResponse;
import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import com.wanted.yamyam.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.wanted.yamyam.global.exception.ErrorCode.LAT_LON_NO_VALUE;

@RequiredArgsConstructor
@Service
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public StoreListResponse storeList(Pageable pageable, String lat, String lon, Double range) {
        if (lat.isEmpty() || lon.isEmpty()) {
            throw new ErrorException(LAT_LON_NO_VALUE);
        }

        List<Store> allList = storeRepository.findAll();

        StoreList response = new StoreList();
        for (int i = 0; i < allList.size(); i++) {
            String listLat = allList.get(i).getLat();
            String listLon = allList.get(i).getLon();
            double distance = findDistance(lat, lon, listLat, listLon);
            log.info("distance = {}", distance);

            if (range >= distance) {
                Store store = allList.get(i);
                response.builder()
                        .lat(store.getLat())
                        .lon(store.getLon())
                        .name(store.getName())
                        .address(store.getAddress())
                        .category(store.getCategory())
                        .rating(store.getRating())
                        .build();
            }
        }
//        Arrays.sort(response, new Comparator<>());
        return new StoreListResponse(response);
    }

    /**
     * 맛집 목록에 있는 위도, 경도와 입력값으로 들어온 위도, 경도의 거리를 구합니다.
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    private double findDistance(String lat1, String lon1, String lat2, String lon2) {
        double pointLat1 = Double.parseDouble(lat1);
        double pointLon1 = Double.parseDouble(lon1);
        double pointLat2 = Double.parseDouble(lat2);
        double pointLon2 = Double.parseDouble(lon2);

        double R = 6371; // km
        double dLat = Math.toRadians(pointLat2 - pointLat1);
        double dLon = Math.toRadians(pointLon2 - pointLon1);

        pointLat1 = Math.toRadians(pointLat1);
        pointLat2 = Math.toRadians(pointLat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(pointLat1) * Math.cos(pointLat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
