package com.java.assessment.reviewbox.global.tmdb.decoder;

import com.java.assessment.reviewbox.domain.actor.business.exception.ActorNotFoundException;
import com.java.assessment.reviewbox.domain.movie.business.exception.MovieNotFoundException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class TmdbErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.NOT_FOUND.value()) {
            if (methodKey.contains("getMovieDetail")) {
                return MovieNotFoundException.EXCEPTION;
            }

            if (methodKey.contains("getMovieCredits")) {
                return MovieNotFoundException.EXCEPTION;
            }

            if (methodKey.contains("getActorDetail")) {
                return ActorNotFoundException.EXCEPTION;
            }

            if (methodKey.contains("getActorMovieCredits")) {
                return ActorNotFoundException.EXCEPTION;
            }
        }

        return FeignException.errorStatus(methodKey, response);
    }
}
