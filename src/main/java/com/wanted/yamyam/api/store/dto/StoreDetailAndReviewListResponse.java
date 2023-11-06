package com.wanted.yamyam.api.store.dto;

import com.wanted.yamyam.api.review.dto.StoreByReviewListResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreDetailAndReviewListResponse {

    private StoreDetailResponse store;

    private List<StoreByReviewListResponse> reviews;

}
