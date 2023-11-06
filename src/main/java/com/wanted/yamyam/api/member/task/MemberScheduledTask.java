package com.wanted.yamyam.api.member.task;

import com.wanted.yamyam.api.store.dto.DiscordWebhook;
import com.wanted.yamyam.api.store.dto.StoreResponse;
import com.wanted.yamyam.api.store.service.StoreService;
import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.member.repo.MemberRepository;
import com.wanted.yamyam.global.config.DiscordConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 사용자와 관련된 정기적인 작업을 정의하는 클래스입니다.
 * @author 정성국
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class MemberScheduledTask {

    private final MemberRepository memberRepository;
    private final StoreService storeService;
    private final DiscordConfig discordConfig;
    private final RestTemplate restTemplate;

    /**
     * 점심 추천 서비스 사용을 체크한 사용자에게 매일 점심 시간에 주변의 맛집을 추천합니다.
     * @author 정성국
     */
    @Scheduled(cron = "0 30 11 * * ?", zone = "Asia/Seoul")
    public void recommendLunch() {
        List<Member> members = memberRepository.findAllByUseRecommendLunch(true);
        members.forEach(member -> {
            List<StoreResponse> stores = getTop5StoresByDistance(member.getLat(), member.getLon(), 0.5);
            var discordWebhook = getDiscordWebhookForRecommendLunch(stores);
            HttpStatusCode statusCode = restTemplate.postForEntity(
                    discordConfig.getWebhookUrl(), discordWebhook, String.class).getStatusCode();

            log.info("recommend lunch for " + member.getUsername() +
                    ", response HTTP Status Code from discord: " + statusCode.value());
        });
    }

    private List<StoreResponse> getTop5StoresByDistance(double lat, double lon, double range) {
        return storeService.storeList("distance", 0, 5,
                Double.toString(lat), Double.toString(lon), range).getStores();
    }

    private DiscordWebhook getDiscordWebhookForRecommendLunch(List<StoreResponse> stores) {
        String message;
        if (stores.isEmpty()) {
            // TODO: 범위를 넓혀 검색한 후 가장 가까운 맛집의 거리와 함께 알려주기
            message = "주변에 맛집이 없네요. ㅜㅜ";
        } else {
            message = "오늘의 근처 점심 맛집을 추천드립니다.\n" + getRecommendedStoreSummary(stores);
        }
        return DiscordWebhook.builder()
                .username("YamYam")
                .content(message)
                .build();
    }

    private String getRecommendedStoreSummary(List<StoreResponse> stores) {
        return stores.stream()
                .map(StoreResponse::toString)
                .collect(Collectors.joining("\n\n"));
    }

}
