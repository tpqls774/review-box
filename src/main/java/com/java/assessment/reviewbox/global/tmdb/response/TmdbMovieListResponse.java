package com.java.assessment.reviewbox.global.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class TmdbMovieListResponse {
    private int page;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

    private List<MovieSummary> results;

    @Getter
    public static class MovieSummary {
        private long id;
        private String title;

        @JsonProperty("release_date")
        private String releaseDate;

        private String overview;

        @JsonProperty("vote_average")
        private double voteAverage;

        @JsonProperty("genre_ids")
        private List<Integer> genreIds;

        @JsonProperty("poster_path")
        private String posterPath;
    }
}
