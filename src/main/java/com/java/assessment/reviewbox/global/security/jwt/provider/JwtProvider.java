package com.java.assessment.reviewbox.global.security.jwt.provider;

import com.java.assessment.reviewbox.global.security.jwt.enums.JwtPurpose;
import com.java.assessment.reviewbox.global.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final SecretKeySpec secretKeySpec;

    public String generateAccessToken(long userId) {
        return buildToken(userId, JwtPurpose.ACCESS, new Date());
    }

    public String generateRefreshToken(long userId) {
        return buildToken(userId, JwtPurpose.REFRESH, new Date());
    }

    private String buildToken(long userId, JwtPurpose purpose, Date now) {
        Date expiration = new Date(now.getTime()
                + Duration.ofSeconds(
                                purpose == JwtPurpose.ACCESS
                                        ? jwtProperties.getAccessExpiration()
                                        : jwtProperties.getRefreshExpiration())
                        .toMillis());

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("purpose", purpose.name().toLowerCase())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKeySpec)
                .compact();
    }
}
