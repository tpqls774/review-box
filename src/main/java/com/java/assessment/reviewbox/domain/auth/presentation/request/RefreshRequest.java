package com.java.assessment.reviewbox.domain.auth.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RefreshRequest {
    @NotBlank(message = "리프레시 토큰은 필수입니다")
    @Size(min = 20, max = 500, message = "리프레시 토큰은 20자 이상 500자 이하이어야 합니다")
    private final String refreshToken;
}
