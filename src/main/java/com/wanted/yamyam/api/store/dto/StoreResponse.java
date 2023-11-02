package com.wanted.yamyam.api.store.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
public class StoreResponse {

    private Double lat;

    private Double lon;

    private String name;

    private String address;

    private String category;

    private Double rating;

    private Double distance;

}
