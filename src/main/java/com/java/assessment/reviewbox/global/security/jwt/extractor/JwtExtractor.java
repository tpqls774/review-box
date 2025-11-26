package com.java.assessment.reviewbox.global.security.jwt.extractor;

import com.java.assessment.reviewbox.global.security.jwt.enums.JwtPurpose;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtExtractor {
    private final SecretKeySpec secretKeySpec;

    public long getSubject(String token) {
        return Long.parseLong(getPayload(token).getSubject());
    }

    public JwtPurpose getPurpose(String token) {
        return JwtPurpose.valueOf(getPayload(token).get("purpose", String.class).toUpperCase());
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(secretKeySpec)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
