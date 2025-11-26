package com.java.assessment.reviewbox.domain.movie.presentation.response;

import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder(toBuilder = true)
public class MovieDetailResponse {
    private final long id;
    private final String title;
    private final int year;
    private final String posterUrl;
    private final String overview;
    private final double rating;
    private final int runtime;
    private final Set<String> genres;
    private final String director;
    private final List<Cast> cast;
    private final long reviewCount;

    public static MovieDetailResponse of(
            long id,
            String title,
            int year,
            String posterUrl,
            String overview,
            double rating,
            int runtime,
            Set<String> genres,
            String director,
            List<Cast> cast,
            long reviewCount) {
        return new MovieDetailResponse(
                id, title, year, posterUrl, overview, rating, runtime, genres, director, cast, reviewCount);
    }

    @RequiredArgsConstructor
    @Getter
    public static class Cast {
        private final long id;
        private final String name;
        private final String character;
        private final String profileUrl;

        public static Cast of(long id, String name, String character, String profileUrl) {
            return new Cast(id, name, character, profileUrl);
        }
    }
}
