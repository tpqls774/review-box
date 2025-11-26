package com.java.assessment.reviewbox.domain.review.business.exception;

import com.java.assessment.reviewbox.domain.review.business.exception.code.ReviewErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class ReviewNotFoundException extends BusinessException {
    public static final ReviewNotFoundException EXCEPTION = new ReviewNotFoundException();

    private ReviewNotFoundException() {
        super(ReviewErrorCode.REVIEW_NOT_FOUND);
    }
}
