package com.wanted.yamyam.api.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record DiscordWebhook(
        @JsonInclude
        String username,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String avatar_url,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String content,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<DiscordWebhookEmbed> embeds
) implements Serializable { }
