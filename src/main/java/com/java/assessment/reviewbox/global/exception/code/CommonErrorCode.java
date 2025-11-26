package com.java.assessment.reviewbox.global.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST.value(), "입력값 검증에 실패했습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 않은 서버 오류가 발생했습니다"),
    MESSAGE_WRITE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "HTTP 응답 작성에 실패했습니다");

    private final int status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
