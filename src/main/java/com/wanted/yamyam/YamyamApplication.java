package com.wanted.yamyam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class YamyamApplication {

    public static void main(String[] args) {
        SpringApplication.run(YamyamApplication.class, args);
    }

}
