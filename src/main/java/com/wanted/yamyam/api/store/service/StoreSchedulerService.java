package com.wanted.yamyam.api.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.yamyam.api.store.dto.response.StoreSchedulerResponse;
import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreSchedulerService {

    @Value("${apiKey}")
    private String apiKey;

    private final StoreRepository storeRepository;

    @Transactional
    public URI getUri(Integer page) {
        return UriComponentsBuilder
                .fromUriString("https://openapi.gg.go.kr/Genrestrtchifood")
                .queryParam("key", apiKey)
                .queryParam("type", "json")
                .queryParam("pIndex", page)
                .queryParam("pSize", 100)
                .encode()
                .build()
                .toUri();
    }

    @Transactional
    @PostConstruct
    public void getStore() throws  JsonProcessingException {
        URI uri = getUri(2);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(uri, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(response);

        // JSON 데이터가 배열 내에 있으므로 배열 요소에 접근합니다.
        JsonNode rows = root.at("/Genrestrtchifood/1/row");

        String json = objectMapper.writeValueAsString(rows);
        List<StoreSchedulerResponse> responses = objectMapper.readValue(json, new TypeReference<List<StoreSchedulerResponse>>() {
        });
        List<Store> store = StoreSchedulerResponse.toListEntity(responses);
        storeRepository.saveAll(store);


    }


}