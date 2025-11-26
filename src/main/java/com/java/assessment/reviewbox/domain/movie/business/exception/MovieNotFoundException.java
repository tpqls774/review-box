package com.java.assessment.reviewbox.domain.movie.business.exception;

import com.java.assessment.reviewbox.domain.movie.business.exception.code.MovieErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class MovieNotFoundException extends BusinessException {
    public static final MovieNotFoundException EXCEPTION = new MovieNotFoundException();

    private MovieNotFoundException() {
        super(MovieErrorCode.MOVIE_NOT_FOUND);
    }
}
