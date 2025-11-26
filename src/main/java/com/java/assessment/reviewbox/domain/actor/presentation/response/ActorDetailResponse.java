package com.java.assessment.reviewbox.domain.actor.presentation.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ActorDetailResponse {
    private final long id;
    private final String name;
    private final String profileUrl;
    private final String birthday;
    private final String birthplace;
    private final String biography;
    private final List<ActorMovieResponse> movies;
}
