package com.wanted.yamyam.api.store.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class StoreResponse {

    private double lat;

    private double lon;

    private String name;

    private String address;

    private String category;

    private double rating;

    private double distance;

    public StoreResponse(double lat, double lon, String name, String address, String category, double rating) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.address = address;
        this.category = category;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "· 상호명: %s / 주소: %s / 종류: %s / 평점: %s / 거리: %s km"
                .formatted(name, address, category,
                        Double.toString(rating).substring(0, 3), Double.toString(distance).substring(0, 4));
    }
}
