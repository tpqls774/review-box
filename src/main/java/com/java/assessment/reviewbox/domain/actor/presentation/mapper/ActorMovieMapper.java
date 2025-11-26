package com.java.assessment.reviewbox.domain.actor.presentation.mapper;

import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorMovieResponse;
import com.java.assessment.reviewbox.global.tmdb.constants.TmdbConstants;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbActorMovieCreditsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ActorMovieMapper {
    @Mapping(target = "posterUrl", source = "posterPath", qualifiedByName = "poster")
    @Mapping(target = "year", source = "releaseDate", qualifiedByName = "year")
    ActorMovieResponse toResponse(TmdbActorMovieCreditsResponse.Movie item);

    @Named("poster")
    default String mapPoster(String path) {
        if (path == null) return null;

        return TmdbConstants.TMDB_IMAGE_BASE_URL + path;
    }

    @Named("year")
    default int toYear(String date) {
        if (date == null || date.length() < 4) return 0;

        return Integer.parseInt(date.substring(0, 4));
    }
}
