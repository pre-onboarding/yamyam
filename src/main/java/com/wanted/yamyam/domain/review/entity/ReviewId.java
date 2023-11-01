package com.wanted.yamyam.domain.review.entity;

import com.wanted.yamyam.domain.member.entity.Member;
import com.wanted.yamyam.domain.store.entity.Store;
import lombok.*;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReviewId implements Serializable {
    private Member member;
    private Store store;
}
