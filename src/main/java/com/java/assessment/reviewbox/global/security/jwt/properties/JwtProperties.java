package com.java.assessment.reviewbox.global.security.jwt.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
@Getter
public class JwtProperties {
    private final String secretKey;
    private final String issuer;
    private final long accessExpiration;
    private final long refreshExpiration;
}
