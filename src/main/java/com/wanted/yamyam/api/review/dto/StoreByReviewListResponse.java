package com.wanted.yamyam.api.review.dto;

import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreByReviewListResponse {

    private Long memberId;

    private int score;

    private String content;

    public StoreByReviewListResponse(Review review) {
        this.memberId = review.getMember().getId();
        this.score = review.getScore();
        this.content = review.getContent();
    }
}
