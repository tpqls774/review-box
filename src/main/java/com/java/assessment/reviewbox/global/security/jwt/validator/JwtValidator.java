package com.java.assessment.reviewbox.global.security.jwt.validator;

import com.java.assessment.reviewbox.domain.auth.business.exception.InvalidTokenException;
import io.jsonwebtoken.Jwts;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private final SecretKeySpec secretKeySpec;

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKeySpec).build().parseSignedClaims(token);
        } catch (Exception e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }
}
