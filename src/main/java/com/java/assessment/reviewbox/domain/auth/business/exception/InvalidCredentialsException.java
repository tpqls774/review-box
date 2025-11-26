package com.java.assessment.reviewbox.domain.auth.business.exception;

import com.java.assessment.reviewbox.domain.auth.business.exception.code.AuthErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class InvalidCredentialsException extends BusinessException {
    public static final InvalidCredentialsException EXCEPTION = new InvalidCredentialsException();

    private InvalidCredentialsException() {
        super(AuthErrorCode.INVALID_CREDENTIALS);
    }
}
