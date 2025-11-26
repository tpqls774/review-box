package com.java.assessment.reviewbox.domain.user.presentation.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MyReviewResponse {
    private final long reviewId;
    private final long movieId;
    private final String movieTitle;
    private final String reviewTitle;
    private final String reviewPreview;
    private final long likeCount;
    private final String createdAt;

    public static MyReviewResponse of(
            long reviewId,
            long movieId,
            String movieTitle,
            String reviewTitle,
            String reviewPreview,
            long likeCount,
            String createdAt) {
        return new MyReviewResponse(reviewId, movieId, movieTitle, reviewTitle, reviewPreview, likeCount, createdAt);
    }
}
