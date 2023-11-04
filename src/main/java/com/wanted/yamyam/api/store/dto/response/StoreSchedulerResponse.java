package com.wanted.yamyam.api.store.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanted.yamyam.domain.store.entity.StoreId;
import lombok.Getter;
import com.wanted.yamyam.domain.store.entity.Store;


import java.util.List;
import java.util.Objects;
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
    @JsonProperty("BSN_STATE_NM")
    private String state;


    public static Store toEntity(StoreSchedulerResponse res) {

        return Store.builder()
                .lat(res.getLat())
                .lon(res.getLon())
                .name(res.getName())
                .address(res.getAddress())
                .category(res.getCategory())
                .build();

    }

    public static List<Store> toListEntity(List<StoreSchedulerResponse> list) {
        return list.stream()
                .filter(dto -> dto.getState().equals("영업"))
                .filter(dto -> dto.getAddress() != null)
                .map(StoreSchedulerResponse::toEntity)
                .collect(Collectors.toList());

    }
}
