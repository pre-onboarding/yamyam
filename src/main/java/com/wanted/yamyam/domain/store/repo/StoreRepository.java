package com.wanted.yamyam.domain.store.repo;

import com.wanted.yamyam.api.store.dto.StoreResponse;
import com.wanted.yamyam.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface StoreRepository extends JpaRepository<Store,Long> {

    @Query("select new com.wanted.yamyam.api.store.dto.StoreResponse(a.lat, a.lon, a.name, a.address, a.category, a.rating, a.lat) from Store a where a.lat >= :lat1 and a.lat <= :lat2 and a.lon >= :lon1 and a.lon <= :lon2")
    List<StoreResponse> findByAllList(@Param("lat1") Double lat1, @Param("lon1") Double lon1, @Param("lat2") Double lat2, @Param("lon2") Double lon2);
}
