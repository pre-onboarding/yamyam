package com.wanted.yamyam.api.store.service;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.wanted.yamyam.api.store.dto.response.StoreSchedulerResponse;
import com.wanted.yamyam.domain.store.entity.Store;
import com.wanted.yamyam.domain.store.repo.StoreRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
    public void getRestaurant() throws ParseException {
        RestTemplate restTemplate = new RestTemplate();

        // 페이지 값을 1로 두었을 때 값이 오는 지 체크하기 위해 추후 수정될 부분
        String jsonString = restTemplate.getForObject(getUri(1), String.class);
        JSONParser jsonParser = new JSONParser(jsonString);
        JSONObject jsonObject = (JSONObject) jsonParser.parse();
        JSONObject jsonObject1 = (JSONObject) jsonObject.get("Genrestrtchifood");
        JSONArray row = (JSONArray) jsonObject1.get("row");
        for (int i = 1; i < row.toArray().length + 1; i++) {
            JSONObject obj = (JSONObject) row.get(i);
            Store store = Store.builder()
                    .id((long) i)
                    .address((String) obj.get("REFINE_LOTNO_ADDR"))
                    .category((String) obj.get("SANITTN_BIZCOND_NM"))
                    .lat((String) obj.get("REFINE_WGS84_LAT"))
                    .lon((String) obj.get("REFINE_WGS84_LAT"))
                    .name((String) obj.get("BIZPLC_NM"))
                    .build();
            storeRepository.save(store);
        }


    }


    @Transactional
    public String connUrl(URI uri) throws IOException {
        URL url = new URL(uri.toString());
        log.info("request url: {}", url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader rd;

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb.toString();

    }
}
