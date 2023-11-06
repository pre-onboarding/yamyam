package com.wanted.yamyam.domain.review.entity;

import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ReviewId.class)
public class Review implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "store_name"),
            @JoinColumn(name = "store_address")
    })
    private Store store;

    @Column
    private int score;

    @Column
    private String content;

}
