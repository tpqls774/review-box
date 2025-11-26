package com.java.assessment.reviewbox.global.tmdb.config;

import com.java.assessment.reviewbox.global.tmdb.decoder.TmdbErrorDecoder;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TmdbClientConfig {
    @Value("${tmdb.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor tmdbApiKeyInterceptor() {
        return (RequestTemplate template) -> {
            template.query("api_key", apiKey);
            template.query("language", "ko-KR");
        };
    }

    @Bean
    public Request.Options tmdbOptions() {
        return new Request.Options(5L, TimeUnit.SECONDS, 10L, TimeUnit.SECONDS, false);
    }

    @Bean
    public Retryer tmdbRetryer() {
        return new Retryer.Default(1000L, 1000L, 2);
    }

    @Bean
    public ErrorDecoder tmdbErrorDecoder() {
        return new TmdbErrorDecoder();
    }
}
