package com.wanted.yamyam.api.store.controller;

import com.wanted.yamyam.api.review.dto.StoreByReviewListResponse;
import com.wanted.yamyam.api.review.service.ReviewService;
import com.wanted.yamyam.api.store.dto.StoreDetailAndReviewListResponse;
import com.wanted.yamyam.api.store.dto.StoreDetailResponse;
import com.wanted.yamyam.api.store.service.StoreService;
import com.wanted.yamyam.api.store.dto.StoreListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stores")
@Slf4j
public class StoreController {

    private final StoreService storeService;

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity storeList(@RequestParam String sort, @RequestParam int page, @RequestParam int pageCount, @RequestParam String lat, @RequestParam String lon, @RequestParam double range) {
        StoreListResponse response = storeService.storeList(sort, page, pageCount, lat, lon, range);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity storeDetail(@PathVariable String storeId) {
        StoreDetailResponse storeDetail = storeService.storeDetail(storeId);
        List<StoreByReviewListResponse> reviewList = reviewService.reviewList(storeId);

        return ResponseEntity.ok().body(new StoreDetailAndReviewListResponse(storeDetail, reviewList));
    }

}
