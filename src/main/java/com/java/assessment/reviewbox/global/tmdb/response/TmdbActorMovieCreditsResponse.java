package com.java.assessment.reviewbox.global.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class TmdbActorMovieCreditsResponse {
    private List<Movie> cast;

    @Getter
    public static class Movie {
        private long id;
        private String title;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("release_date")
        private String releaseDate;

        private String character;
    }
}
