package com.wanted.yamyam.domain.store.repo;

import com.wanted.yamyam.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store,Long> {
    @Modifying
    @Query("UPDATE Store SET rating = ?2 WHERE id = ?1")
    void updateRatingById(Long id, String rating);
}
