package com.java.assessment.reviewbox.domain.movie.business.exception;

import com.java.assessment.reviewbox.domain.movie.business.exception.code.MovieErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class GenreNotFoundException extends BusinessException {
    public static final GenreNotFoundException EXCEPTION = new GenreNotFoundException();

    private GenreNotFoundException() {
        super(MovieErrorCode.GENRE_NOT_FOUND);
    }
}
