package com.java.assessment.reviewbox.domain.auth.presentation.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RefreshResponse {
    private final String accessToken;
    private final String refreshToken;

    public static RefreshResponse of(String accessToken, String refreshToken) {
        return new RefreshResponse(accessToken, refreshToken);
    }
}
