package com.wanted.yamyam.api.store.controller;

import com.wanted.yamyam.api.store.service.StoreService;
import com.wanted.yamyam.api.store.dto.StoreListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity storeList(@RequestParam String sort, @RequestParam int page, @RequestParam int pageCount, @RequestParam String lat, @RequestParam String lon, @RequestParam double range) {
        StoreListResponse response = storeService.storeList(sort, page, pageCount, lat, lon, range);

        return ResponseEntity.ok().body(response);
    }

}
