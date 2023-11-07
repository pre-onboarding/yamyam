package com.wanted.yamyam.domain.store.repo;

import com.wanted.yamyam.api.store.dto.StoreResponse;
import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.entity.StoreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import java.util.List;


public interface StoreRepository extends JpaRepository<Store, StoreId> ,StoreRepositoryCustom{


    @Query("select new com.wanted.yamyam.api.store.dto.StoreResponse(a.lat, a.lon, a.name, a.address, a.category, a.rating) from Store a where a.lat >= :lat1 and a.lat <= :lat2 and a.lon >= :lon1 and a.lon <= :lon2")
    List<StoreResponse> findByAllList(@Param("lat1") double lat1, @Param("lon1") double lon1, @Param("lat2") double lat2, @Param("lon2") double lon2);
  
    @Modifying
    @Query("UPDATE Store SET rating = ?3 WHERE name = ?1 AND address = ?2")
    void updateRatingById(String storeName, String StoreAddress, double rating);
}

    

