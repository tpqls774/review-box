package com.java.assessment.reviewbox.domain.review.business.exception.code;

import com.java.assessment.reviewbox.global.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ReviewErrorCode implements ErrorCode {
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "리뷰를 찾을 수 없습니다"),
    INVALID_REVIEW_ACCESS(HttpStatus.BAD_REQUEST.value(), "해당 영화의 리뷰가 아닙니다"),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST.value(), "이미 좋아요를 누른 리뷰입니다"),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "좋아요를 찾을 수 없습니다");

    private final int status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
