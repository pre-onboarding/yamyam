package com.wanted.yamyam.api.store.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
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
}
