package com.java.assessment.reviewbox.domain.movie.presentation.mapper;

import com.java.assessment.reviewbox.domain.movie.persistence.enums.MovieGenre;
import com.java.assessment.reviewbox.domain.movie.presentation.response.MovieResponse;
import com.java.assessment.reviewbox.global.tmdb.constants.TmdbConstants;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbMovieListResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    @Mapping(target = "id", source = "response.id")
    @Mapping(target = "title", source = "response.title")
    @Mapping(target = "year", source = "response.releaseDate", qualifiedByName = "year")
    @Mapping(target = "posterUrl", source = "response.posterPath", qualifiedByName = "poster")
    @Mapping(target = "overview", source = "response.overview")
    @Mapping(target = "rating", source = "response.voteAverage")
    @Mapping(target = "genres", source = "response.genreIds", qualifiedByName = "genres")
    MovieResponse toResponse(TmdbMovieListResponse.MovieSummary response);

    @Named("year")
    default int toYear(String releaseDate) {
        if (releaseDate == null || releaseDate.length() < 4) return 0;

        return Integer.parseInt(releaseDate.substring(0, 4));
    }

    @Named("poster")
    default String mapPosterPath(String posterPath) {
        if (posterPath == null) return null;

        return TmdbConstants.TMDB_IMAGE_BASE_URL + posterPath;
    }

    @Named("genres")
    default Set<String> toGenres(List<Integer> genreIds) {
        if (genreIds == null) return Set.of();

        return genreIds.stream().map(MovieGenre::fromTmdbId).map(Enum::name).collect(Collectors.toSet());
    }
}
