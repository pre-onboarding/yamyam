package com.wanted.yamyam.api.store;

import com.wanted.yamyam.api.store.dto.StoreList;
import com.wanted.yamyam.api.store.dto.StoreListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stores")
@Slf4j
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity storeList(Pageable pageable, @RequestParam String lat, @RequestParam String lon, @RequestParam Double range) {
        StoreListResponse response = storeService.storeList(pageable, lat, lon, range);

        return ResponseEntity.ok().body(response);
    }

}
