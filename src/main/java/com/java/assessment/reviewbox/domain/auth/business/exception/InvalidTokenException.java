package com.java.assessment.reviewbox.domain.auth.business.exception;

import com.java.assessment.reviewbox.domain.auth.business.exception.code.AuthErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class InvalidTokenException extends BusinessException {
    public static final InvalidTokenException EXCEPTION = new InvalidTokenException();

    private InvalidTokenException() {
        super(AuthErrorCode.INVALID_TOKEN);
    }
}
