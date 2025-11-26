package com.java.assessment.reviewbox.global.response;

import com.java.assessment.reviewbox.global.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;

    public static ErrorResponse of(int status, String code, String message) {
        return new ErrorResponse(status, code, message);
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return of(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }
}
