package com.java.assessment.reviewbox.domain.actor.presentation.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActorSearchResponse {
    private final long id;
    private final String name;
    private final String profileUrl;
    private final String knownForDepartment;
    private final List<KnownFor> knownFor;

    @Getter
    @RequiredArgsConstructor
    public static class KnownFor {
        private final long id;
        private final String title;
        private final String posterUrl;
        private final String mediaType;

        public static KnownFor of(long id, String title, String posterUrl, String mediaType) {
            return new KnownFor(id, title, posterUrl, mediaType);
        }
    }

    public static ActorSearchResponse of(
            long id, String name, String profileUrl, String knownForDepartment, List<KnownFor> knownFor) {
        return new ActorSearchResponse(id, name, profileUrl, knownForDepartment, knownFor);
    }
}
