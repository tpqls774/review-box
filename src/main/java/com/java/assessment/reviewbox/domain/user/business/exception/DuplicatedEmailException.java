package com.java.assessment.reviewbox.domain.user.business.exception;

import com.java.assessment.reviewbox.domain.user.business.exception.code.UserErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class DuplicatedEmailException extends BusinessException {
    public static final DuplicatedEmailException EXCEPTION = new DuplicatedEmailException();

    private DuplicatedEmailException() {
        super(UserErrorCode.DUPLICATED_EMAIL);
    }
}
