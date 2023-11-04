package com.wanted.yamyam.api.store.service;

import com.wanted.yamyam.api.store.dto.StoreListResponse;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Test
    @DisplayName("맛집 목록 조회")
    @Transactional(readOnly = true)
    void storeList() {
        // given
        String sortByDistance = "distance";
        String sortByRating = "rating";
        int page = 0;
        int pageCount = 10;
        String lat = "38.0362201660";
        String lon = "127.3675276602";
        double range = 100.0;

        // when
        StoreListResponse response = storeService.storeList(sortByDistance, page, pageCount, lat, lon, range);
        StoreListResponse response2 = storeService.storeList(sortByRating, page, pageCount, lat, lon, range);

        // then
        assertThat(response.getStores()).isNotNull();
        assertThat(response2.getStores()).isNotNull();
    }
}