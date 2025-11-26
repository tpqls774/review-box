package com.java.assessment.reviewbox.domain.auth.business.exception.code;

import com.java.assessment.reviewbox.global.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST.value(), "이메일 또는 비밀번호가 올바르지 않습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다");

    private final int status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
