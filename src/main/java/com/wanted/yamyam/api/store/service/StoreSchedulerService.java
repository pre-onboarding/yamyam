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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class StoreSchedulerService {

    @Value("${apiKey}")
    private String apiKey;
    private static final String CHINA_FOOD_URL = "https://openapi.gg.go.kr/Genrestrtchifood";
    private static final String SOUP_URL = "https://openapi.gg.go.kr/Genrestrtsoup";
    private static final String JAPAN_FOOD_URL = "https://openapi.gg.go.kr/Genrestrtjpnfood";
    private static final String CHINA_FOOD = "Genrestrtchifood";
    private static final String SOUP = "Genrestrtsoup";
    private static final String JAPAN_FOOD = "Genrestrtjpnfood";
    private final StoreRepository storeRepository;

    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    // 개발할 때는 즉시 데이터를 넣어주는 @PostConstruct 추천
//    @PostConstruct
    public void getData() throws JsonProcessingException {
        getStore(CHINA_FOOD_URL, CHINA_FOOD);
        getStore(JAPAN_FOOD_URL, JAPAN_FOOD);
        getStore(SOUP_URL, SOUP);
    }

    @Transactional
    public void getStore(String url, String str) throws JsonProcessingException {

        int maxPage = getMaxPage(url, str);
        for (int j = 1; j <= maxPage; j++) {
            URI uri = getUri(j, url);
            saveStore(uri,str);
        }
    }

    @Transactional(readOnly = true)
    public void saveStore(URI uri,String str) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(uri, String.class);
        List<StoreSchedulerResponse> responses = getListStoreDto(response, str);
        List<Store> store = StoreSchedulerResponse.toListEntity(responses);
        storeRepository.saveAll(store);
    }

    @Transactional(readOnly = true)
    public int getMaxPage(String url, String str) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        URI uri = getUri(1, url);

        String response = restTemplate.getForObject(uri, String.class);

        JsonNode root = objectMapper.readTree(response);

        // JSON 데이터가 배열 내에 있으므로 배열 요소에 접근합니다.
        JsonNode head = root.at("/" + str + "/0/head");

        int totalCount = head.get(0).get("list_total_count").asInt();
        int maxPage = totalCount / 100;
        if (totalCount % 100 > 0) {
            maxPage++;
        }
        return maxPage;
    }


    @Transactional(readOnly = true)
    public URI getUri(int page, String url) {
        return UriComponentsBuilder
                .fromUriString(url)
                .queryParam("key", apiKey)
                .queryParam("type", "json")
                .queryParam("pIndex", page)
                .queryParam("pSize", 100)
                .encode()
                .build()
                .toUri();
    }


    @Transactional(readOnly = true)
    public List<StoreSchedulerResponse> getListStoreDto(String response, String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();


        JsonNode root = objectMapper.readTree(response);

        // JSON 데이터가 배열 내에 있으므로 배열 요소에 접근합니다.
        JsonNode rows = root.at("/" + str + "/1/row");

        String json = objectMapper.writeValueAsString(rows);
        return objectMapper.readValue(json, new TypeReference<List<StoreSchedulerResponse>>() {
        });

    }




}