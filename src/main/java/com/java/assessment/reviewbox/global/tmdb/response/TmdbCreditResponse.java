package com.java.assessment.reviewbox.global.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class TmdbCreditResponse {
    private List<Cast> cast;
    private List<Crew> crew;

    @Getter
    public static class Cast {
        private long id;
        private String name;
        private String character;

        @JsonProperty("profile_path")
        private String profilePath;
    }

    @Getter
    public static class Crew {
        private long id;
        private String name;
        private String department;
        private String job;

        @JsonProperty("profile_path")
        private String profilePath;
    }
}
