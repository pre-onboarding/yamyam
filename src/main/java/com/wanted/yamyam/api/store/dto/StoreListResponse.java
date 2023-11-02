package com.wanted.yamyam.api.store.dto;

import com.wanted.yamyam.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreListResponse {

    List<StoreResponse> stores;
    int total;

}
