package com.java.assessment.reviewbox.global.security.jwt.config;

import com.java.assessment.reviewbox.global.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    private final JwtProperties jwtProperties;

    @Bean
    public SecretKeySpec secretKeySpec() {
        return new SecretKeySpec(
                jwtProperties.getSecretKey().getBytes(),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }
}
