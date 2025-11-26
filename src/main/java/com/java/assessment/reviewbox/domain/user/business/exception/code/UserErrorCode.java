package com.java.assessment.reviewbox.domain.user.business.exception.code;

import com.java.assessment.reviewbox.global.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다"),
    INVALID_USERNAME(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 사용자명입니다"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이메일입니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 비밀번호입니다"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT.value(), "이미 사용 중인 이메일입니다");

    private final int status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
