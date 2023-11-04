package com.wanted.yamyam.api.store.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class StoreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("맛집 조회 목록")
    void storeList() throws Exception {
        // given
        String sort = "distance";
        int page = 0;
        int pageCount = 10;
        String lat = "38.0362201660";
        String lon = "127.3675276602";
        double range = 100.0;

        // when
        ResultActions resultActions = mvc.perform(get("/api/v1/stores?sort="
                + sort + "&page=" + page + "&pageCount=" + pageCount + "&lat=" + lat + "&lon=" + lon + "&range=" + range)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk());
    }
}