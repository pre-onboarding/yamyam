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
    private static final String chinaFoodUrl = "https://openapi.gg.go.kr/Genrestrtchifood";
    private static final String soupUrl = "https://openapi.gg.go.kr/Genrestrtsoup";
    private static final String japanFoodUrl = "https://openapi.gg.go.kr/Genrestrtjpnfood";
    private final StoreRepository storeRepository;


    @PostConstruct
    public void getData() throws JsonProcessingException {
        getStore(chinaFoodUrl,"Genrestrtchifood");
        getStore(soupUrl,"Genrestrtsoup");
        getStore(japanFoodUrl,"Genrestrtjpnfood");
    }
    @Transactional
    public URI getUri(Integer page,String url) {
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

    @Transactional
    public void getStore(String url,String str) throws  JsonProcessingException {
        URI uri = getUri(1,url);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(uri, String.class);
        int i = checkTotalCount(response,str);
        int i1 = i/100 +1;
        if(i%100>0){
            i1 ++;
        }
        for (int j = 1; j < i1; j++) {
            URI uri2 = getUri(j,url);
            String response2 = restTemplate.getForObject(uri2, String.class);
            List<StoreSchedulerResponse> responses = getStore2(response2,str);
            List<Store> store = StoreSchedulerResponse.toListEntity(responses);
            storeRepository.saveAll(store);
        }
    }

    public int checkTotalCount(String response,String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(response);

        // JSON 데이터가 배열 내에 있으므로 배열 요소에 접근합니다.
        JsonNode head = root.at("/"+str+"/0/head");
        log.info("a");
        return head.get(0).get("list_total_count").asInt();

    }
    
    public List<StoreSchedulerResponse>  getStore2(String  response,String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(response);

        // JSON 데이터가 배열 내에 있으므로 배열 요소에 접근합니다.
        JsonNode rows = root.at("/"+str+"/1/row");

        String json = objectMapper.writeValueAsString(rows);
        return objectMapper.readValue(json, new TypeReference<List<StoreSchedulerResponse>>() {});
        
    }


}