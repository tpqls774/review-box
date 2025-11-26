package com.java.assessment.reviewbox.domain.movie.presentation.mapper;

import com.java.assessment.reviewbox.domain.movie.persistence.enums.MovieGenre;
import com.java.assessment.reviewbox.domain.movie.presentation.response.MovieDetailResponse;
import com.java.assessment.reviewbox.global.tmdb.constants.TmdbConstants;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbMovieDetailResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", builder = @Builder)
public interface MovieDetailMapper {
    @Mapping(target = "id", source = "detail.id")
    @Mapping(target = "title", source = "detail.title")
    @Mapping(target = "year", source = "detail.releaseDate", qualifiedByName = "year")
    @Mapping(target = "posterUrl", source = "detail.posterPath", qualifiedByName = "poster")
    @Mapping(target = "overview", source = "detail.overview")
    @Mapping(target = "rating", source = "detail.voteAverage")
    @Mapping(target = "runtime", source = "detail.runtime")
    @Mapping(target = "genres", source = "detail.genres", qualifiedByName = "genres")
    MovieDetailResponse toResponse(TmdbMovieDetailResponse detail);

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
    default Set<String> toGenres(List<TmdbMovieDetailResponse.Genre> list) {
        if (list == null) return Set.of();

        return list.stream()
                .map(g -> MovieGenre.fromTmdbId(g.getId()))
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
