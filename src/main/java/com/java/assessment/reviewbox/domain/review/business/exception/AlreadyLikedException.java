package com.java.assessment.reviewbox.domain.review.business.exception;

import com.java.assessment.reviewbox.domain.review.business.exception.code.ReviewErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class AlreadyLikedException extends BusinessException {
    public static final AlreadyLikedException EXCEPTION = new AlreadyLikedException();

    private AlreadyLikedException() {
        super(ReviewErrorCode.ALREADY_LIKED);
    }
}
