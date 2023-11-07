package com.wanted.yamyam.api.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.yamyam.api.review.dto.ReviewRequest;
import com.wanted.yamyam.api.review.dto.ReviewResponse;
import com.wanted.yamyam.api.review.service.ReviewService;
import com.wanted.yamyam.api.store.service.StoreService;
import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.review.entity.Review;
import com.wanted.yamyam.domain.store.entity.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewService reviewService;

    @MockBean
    StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper;

    JacksonTester<ReviewRequest> requestJson;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    @DisplayName("정상적인 맛집 평가 생성 요청")
    void shouldSaveReviewAboutStoreAndReturn201() throws Exception {
        var testScore = 5;
        var testContent = "맛있어요!!";
        var testUsername = "식신";
        var testMember = Member.builder().id(1L).username(testUsername).build();
        var testStore = Store.builder().name("영화루").address("경기도").build();
        var testReviewRequest = new ReviewRequest(testScore, testContent);
        var testReview = Review.builder()
                .member(testMember)
                .store(testStore)
                .score(testReviewRequest.score())
                .content(testReviewRequest.content())
                .build();

        given(reviewService.saveReview(any(Review.class))).willReturn(testReview);

        var reviewResponse = new ReviewResponse(
                testMember.getId(),
                testStore.getName() + testStore.getAddress(),
                testUsername, testScore, testContent);

        mockMvc.perform(post("/api/v1/stores/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson.write(testReviewRequest).getJson()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/stores/1/reviews"))
                .andExpect(jsonPath("$.['memberId']").value(reviewResponse.memberId()))
                .andExpect(jsonPath("$.['storeId']").value(reviewResponse.storeId()))
                .andExpect(jsonPath("$.['username']").value(reviewResponse.username()))
                .andExpect(jsonPath("$.['score']").value(reviewResponse.score()))
                .andExpect(jsonPath("$.['content']").value(reviewResponse.content()));

    }

}
