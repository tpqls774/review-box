package com.java.assessment.reviewbox.global.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TmdbActorDetailResponse {
    private long id;
    private String name;

    @JsonProperty("profile_path")
    private String profilePath;

    private String birthday;

    @JsonProperty("place_of_birth")
    private String placeOfBirth;

    private String biography;
}
