package com.java.assessment.reviewbox.domain.movie.business.exception.code;

import com.java.assessment.reviewbox.global.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MovieErrorCode implements ErrorCode {
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "영화를 찾을 수 없습니다"),
    GENRE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "장르를 찾을 수 없습니다");

    private final int status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
