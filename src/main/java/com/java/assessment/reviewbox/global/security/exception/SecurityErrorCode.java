package com.java.assessment.reviewbox.global.security.exception;

import com.java.assessment.reviewbox.global.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SecurityErrorCode implements ErrorCode {
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED.value(), "인증에 실패했습니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다");

    private final int status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
