package com.java.assessment.reviewbox.domain.user.presentation.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LikedReviewResponse {
    private final long reviewId;
    private final long movieId;
    private final String movieTitle;
    private final String reviewTitle;
    private final String reviewPreview;
    private final long likeCount;
    private final String createdAt;
    private final User user;

    public static LikedReviewResponse of(
            long reviewId,
            long movieId,
            String movieTitle,
            String reviewTitle,
            String reviewPreview,
            long likeCount,
            String createdAt,
            long userId,
            String username) {
        return new LikedReviewResponse(
                reviewId,
                movieId,
                movieTitle,
                reviewTitle,
                reviewPreview,
                likeCount,
                createdAt,
                User.of(userId, username));
    }

    @RequiredArgsConstructor
    @Getter
    public static class User {
        private final long id;
        private final String name;

        public static User of(long id, String name) {
            return new User(id, name);
        }
    }
}
