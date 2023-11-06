package com.wanted.yamyam.api.store.dto;

import com.wanted.yamyam.api.review.dto.StoreByReviewListResponse;
import com.wanted.yamyam.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreDetailResponse {

    private Long id;

    private double lat;

    private double lon;

    private String name;

    private String address;

    private String category;

    private double rating;


    public StoreDetailResponse(Store store) {
        this.id = store.getId();
        this.lat = store.getLat();
        this.lon = store.getLon();
        this.name = store.getName();
        this.address = store.getAddress();
        this.category = store.getCategory();
        this.rating = store.getRating();
    }
}
