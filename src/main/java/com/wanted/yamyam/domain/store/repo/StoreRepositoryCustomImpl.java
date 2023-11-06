package com.wanted.yamyam.domain.store.repo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.yamyam.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanted.yamyam.domain.store.entity.QStore.store;
@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

}
