package com.wanted.yamyam.domain.review.entity;

import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ReviewId.class)
public class Review {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column
    private int score;

    @Column
    private String content;

}
