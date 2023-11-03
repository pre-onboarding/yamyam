package com.wanted.yamyam.domain.store.repo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.wanted.yamyam.domain.store.entity.QStore.store;
@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public long countDeleteStore(){
        return jpaQueryFactory
                .delete(store)
                .where(store.state.eq("폐업"))
                .execute();
    }

//    @Override
//    public long countNull(){
//        return jpaQueryFactory
//                .select(store.count())
//                .where(store.lon.isNull()
//                        .or(store.address.isNull())
//                        .or(store.lat.isNull())
//                )
//                .fetchOne();
//    }
}
