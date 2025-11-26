package com.java.assessment.reviewbox.domain.review.presentation.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReviewResponse {
    private final long id;
    private final String title;
    private final long likes;
    private final Writer writer;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewResponse of(
            long id, String title, long likes, Writer writer, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new ReviewResponse(id, title, likes, writer, createdAt, updatedAt);
    }

    @RequiredArgsConstructor
    @Getter
    public static class Writer {
        private final long id;
        private final String username;

        public static Writer of(long id, String username) {
            return new Writer(id, username);
        }
    }
}
