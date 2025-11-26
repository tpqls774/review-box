package com.java.assessment.reviewbox.domain.review.business.exception;

import com.java.assessment.reviewbox.domain.review.business.exception.code.ReviewErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class InvalidReviewAccessException extends BusinessException {
    public static final InvalidReviewAccessException EXCEPTION = new InvalidReviewAccessException();

    private InvalidReviewAccessException() {
        super(ReviewErrorCode.INVALID_REVIEW_ACCESS);
    }
}
