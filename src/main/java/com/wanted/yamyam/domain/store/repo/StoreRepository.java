package com.wanted.yamyam.domain.store.repo;

import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.entity.StoreId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, StoreId> ,StoreRepositoryCustom{


}
