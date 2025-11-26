package com.java.assessment.reviewbox.domain.user.business.exception;

import com.java.assessment.reviewbox.domain.user.business.exception.code.UserErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
    public static final UserNotFoundException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }
}
