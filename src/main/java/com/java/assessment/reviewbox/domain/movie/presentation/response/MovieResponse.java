package com.java.assessment.reviewbox.domain.movie.presentation.response;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MovieResponse {
    private final long id;
    private final String title;
    private final int year;
    private final String posterUrl;
    private final String overview;
    private final double rating;
    private final Set<String> genres;

    public static MovieResponse of(
            long id, String title, int year, String posterUrl, String overview, double rating, Set<String> genres) {
        return new MovieResponse(id, title, year, posterUrl, overview, rating, genres);
    }
}
