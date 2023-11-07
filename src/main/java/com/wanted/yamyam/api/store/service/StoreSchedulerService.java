package com.wanted.yamyam.api.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.yamyam.api.store.dto.StoreSchedulerResponse;
import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import com.wanted.yamyam.global.exception.ErrorCode;
import com.wanted.yamyam.global.exception.ErrorException;
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

    /*
    * 스케줄러를 실행할 상위 메소드
    * 하위 메소드 안에 url값과 상위 JsonNode를 넣어줍니다.
    * */
    // 개발할 때는 1초 뒤 바로 데이터를 넣어주는 @Scheduled(initialDelay) 추천
//    @Scheduled(initialDelay = 1000 ,fixedDelay = 60 * 60 * 1000)
    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void getData() throws JsonProcessingException {
        getStore(CHINA_FOOD_URL, CHINA_FOOD);
        getStore(JAPAN_FOOD_URL, JAPAN_FOOD);
        getStore(SOUP_URL, SOUP);
    }
    /*
    * maxPage 메소드 실행 후 maxPage 만큼 반복해서 url 실행 및 saveStore 메소드 실행
    * */
    @Transactional
    public void getStore(String url, String str) throws JsonProcessingException {
        int maxPage = getMaxPage(url, str);
        for (int j = 1; j <= maxPage; j++) {
            URI uri = getUri(j, url);
            saveStore(uri,str);
        }
    }

    /*
    * url에 가장 첫 번째 페이지에 접속 후
    * list_total_count 값 파싱 후
    * page 수 계산 메소드
    * */
    @Transactional(readOnly = true)
    public int getMaxPage(String url, String str) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        URI uri = getUri(1, url);

        String response = restTemplate.getForObject(uri, String.class);

        JsonNode root = objectMapper.readTree(response);

        errorApi(root,str);

        // JSON 데이터가 배열 내에 있으므로 배열 요소에 접근합니다.
        JsonNode head = root.at("/" + str + "/0/head");

        int totalCount = head.get(0).get("list_total_count").asInt();
        int maxPage = totalCount / 100;
        if (totalCount % 100 > 0) {
            maxPage++;
        }
        return maxPage;
    }

    /*
    * page와 url 값을 받아서
    * URI 값 만들어주는 메소드
    * */
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

    /*
    * error가 뜰 경우
    * openApi에서 제공하는 예외처리 반영
    * */
    public void errorApi(JsonNode root,String str){
        JsonNode errorCheck = root.at("/" + str + "/0/head/1/RESULT");
        if (errorCheck.isNull() || !errorCheck.has("CODE")) {
            JsonNode result = root.at("/RESULT");
            String message = result.get("MESSAGE").asText();
            throw new ErrorException(message,ErrorCode.OPENAPI_ERROR);
        }
    }

    /*
    * getListStoreDto로 List<StoreSchedulerResponse> 결과 반환 후
    * List<Store> 으로 변경 후 저장
    * */
    @Transactional(readOnly = true)
    public void saveStore(URI uri,String str) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(uri, String.class);
        List<StoreSchedulerResponse> responses = getListStoreDto(response, str);
        if( responses == null || responses.isEmpty() ){
            throw new ErrorException(ErrorCode.INVALID_FILE_LIST);
        }
        List<Store> store = StoreSchedulerResponse.toListEntity(responses);

        storeRepository.saveAll(store);
    }

    /*
    * jsonString 값을 List<StoreSchedulerResponse>로 json 파싱해서 반환해줌
     * */
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