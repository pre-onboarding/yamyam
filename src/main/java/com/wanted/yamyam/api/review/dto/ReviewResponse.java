package com.wanted.yamyam.api.review.dto;

import java.io.Serializable;

public record ReviewResponse(
        long memberId,
        long storeId,
        String username,
        int score,
        String content
) implements Serializable { }
