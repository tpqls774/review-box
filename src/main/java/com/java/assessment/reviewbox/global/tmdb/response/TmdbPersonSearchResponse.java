package com.java.assessment.reviewbox.global.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class TmdbPersonSearchResponse {
    private int page;
    private List<Person> results;
    private int total_pages;
    private int total_results;

    @Getter
    public static class Person {
        private long id;
        private String name;

        @JsonProperty("profile_path")
        private String profilePath;

        @JsonProperty("known_for_department")
        private String knownForDepartment;

        @JsonProperty("known_for")
        private List<KnownFor> knownFor;

        @Getter
        public static class KnownFor {
            private long id;
            private String title;

            @JsonProperty("poster_path")
            private String posterPath;

            @JsonProperty("media_type")
            private String mediaType;
        }
    }
}
