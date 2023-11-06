package com.wanted.yamyam.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("discord")
@Getter
@Setter
public class DiscordConfig {

    private String webhookUrl;

}
