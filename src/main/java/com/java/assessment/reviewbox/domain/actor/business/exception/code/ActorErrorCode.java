package com.java.assessment.reviewbox.domain.actor.business.exception.code;

import com.java.assessment.reviewbox.global.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ActorErrorCode implements ErrorCode {
    ACTOR_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "배우를 찾을 수 없습니다.");

    private final int status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
