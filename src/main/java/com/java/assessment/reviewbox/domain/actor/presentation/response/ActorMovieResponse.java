package com.java.assessment.reviewbox.domain.actor.presentation.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ActorMovieResponse {
    private final long id;
    private final String title;
    private final String posterUrl;
    private final int year;
    private final String character;
}
