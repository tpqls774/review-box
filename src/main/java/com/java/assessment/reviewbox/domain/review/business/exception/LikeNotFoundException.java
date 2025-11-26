package com.java.assessment.reviewbox.domain.review.business.exception;

import com.java.assessment.reviewbox.domain.review.business.exception.code.ReviewErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class LikeNotFoundException extends BusinessException {
    public static final LikeNotFoundException EXCEPTION = new LikeNotFoundException();

    private LikeNotFoundException() {
        super(ReviewErrorCode.LIKE_NOT_FOUND);
    }
}
