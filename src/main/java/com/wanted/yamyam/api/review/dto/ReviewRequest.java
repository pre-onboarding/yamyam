package com.wanted.yamyam.api.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record ReviewRequest(
        @Min(0)
        @Max(5)
        int score,
        @Length(max = 255)
        String content
) {
}
