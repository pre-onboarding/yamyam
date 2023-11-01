package com.wanted.yamyam.domain.store.repo;

import com.wanted.yamyam.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {

}
