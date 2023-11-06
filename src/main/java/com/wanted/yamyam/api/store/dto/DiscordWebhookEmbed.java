package com.wanted.yamyam.api.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record DiscordWebhookEmbed (
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Author author,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String title,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String url,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String description,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Integer color,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<Field> fields,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Image thumbnail,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Image image,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Footer footer

) implements Serializable {
    @Builder
    public record Author (
            @JsonInclude
            String name,
            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            String url,
            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            String icon_url
    ) {}

    @Builder
    public record Field (
            @JsonInclude
            String name,
            @JsonInclude
            String value,
            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            Boolean inline
    ) implements Serializable {}

    @Builder
    public record Image (
            @JsonInclude
            String url
    ) implements Serializable {}

    @Builder
    public record Footer (
            @JsonInclude
            String text,
            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            String icon_url
    ) implements Serializable {}
}
