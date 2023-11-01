package com.wanted.yamyam.api.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class StoreList {

    private String lat;

    private String lon;

    private String name;

    private String address;

    private String category;

    private Double rating;

    public StoreList(String lat, String lon, String name, String address, String category, Double rating) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.address = address;
        this.category = category;
        this.rating = rating;
    }
}
