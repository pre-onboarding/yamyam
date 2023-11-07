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
    @JsonProperty("BSN_STATE_NM")
    private String state;


    // StoreSchedulerResponse를 Store로 반환해줌
    public static Store toEntity(StoreSchedulerResponse res) {

        return Store.builder()
                .lat(Double.parseDouble(res.getLat()))
                .lon(Double.parseDouble(res.getLon()))
                .name(res.getName())
                .address(res.getAddress())
                .category(res.getCategory())
                .build();

    }

    /*
    * dto가 영업이거나 id값인 address가 null이 아닐경우
    * 리스트에서 받은 dto들을 toEntity 반환해서 store값으로 변경
    * store 값들을 Collectors.toList를 통해 List<Store>로 변경
    * */
    public static List<Store> toListEntity(List<StoreSchedulerResponse> list) {
        return list.stream()
                .filter(dto -> dto.getState().equals("영업"))
                .filter(dto -> dto.getAddress() != null)
                .map(StoreSchedulerResponse::toEntity)
                .collect(Collectors.toList());

    }
}
