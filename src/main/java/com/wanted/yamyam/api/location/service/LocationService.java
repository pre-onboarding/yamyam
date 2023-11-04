package com.wanted.yamyam.api.location.service;

import com.wanted.yamyam.domain.location.entity.Location;
import com.wanted.yamyam.domain.location.repo.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 시군구 데이터 처리를 위한 Service 컴포넌트 입닌다.
 * @author 정성국
 */

@RequiredArgsConstructor
@Service
public class LocationService {

    private static final String KEY = "locations";
    private final LocationRepository locationRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 업로드된 시군구 데이터를 받아 기존 데이터 전체 삭제 후 새로 저장합니다.
     * @param locations 업로드된 시군구 데이터
     * @return 저장한 시군구 데이터의 총 갯수를 반환합니다.
     * @author 정성국
     */
    @Transactional
    public int saveAllLocations(List<Location> locations) {
        locationRepository.deleteAllInBatch();
        locations = locationRepository.saveAll(locations);
        saveLocationsToRedis(locations);
        return locations.size();
    }

    /**
     * 저장된 전체 시군구 데이터를 반환합니다.
     * @return 전체 시군구 데이터 목록
     */
    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        List<Location> locations = getLocationsFromRedis();
        if (locations == null) {
            locations = locationRepository.findAll();
            saveLocationsToRedis(locations);
        }
        return locations;
    }

    private void saveLocationsToRedis(List<Location> locations) {
        redisTemplate.opsForValue().set(KEY, locations);
    }

    private List<Location> getLocationsFromRedis() {
        return (List<Location>) redisTemplate.opsForValue().get("locations");
    }

}
