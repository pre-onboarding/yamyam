package com.wanted.yamyam.api.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreListResponse {

    List<StoreList> stores;
    int total;
}
