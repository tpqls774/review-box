package com.java.assessment.reviewbox.global.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class TmdbMovieDetailResponse {
    private long id;

    private String title;

    @JsonProperty("release_date")
    private String releaseDate;

    private String overview;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("poster_path")
    private String posterPath;

    private Integer runtime;
    private List<Genre> genres;

    @Getter
    public static class Genre {
        private int id;
        private String name;
    }
}
