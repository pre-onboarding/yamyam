package com.wanted.yamyam.api.store.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import com.wanted.yamyam.domain.store.entity.Store;


import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreSchedulerResponse {
    @JsonProperty("REFINE_WGS84_LAT")
    private String lat;
    @JsonProperty("REFINE_WGS84_LOGT")
    private String lon;
    @JsonProperty("BIZPLC_NM")
    private String name;
    @JsonProperty("REFINE_ROADNM_ADDR")
    private String address;
    @JsonProperty("SANITTN_BIZCOND_NM")
    private String category;

    public static Store toEntity(StoreSchedulerResponse res){
        return Store.builder()
                .lat(res.getLat())
                .lon(res.getLon())
                .address(res.getAddress())
                .name(res.getName())
                .category(res.getCategory())
                .build();
    }

    public static List<Store> toListEntity(List<StoreSchedulerResponse> item){
        return item.stream()
                .map(StoreSchedulerResponse::toEntity)
                .collect(Collectors.toList());
    }
}
