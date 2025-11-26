package com.java.assessment.reviewbox.domain.review.presentation.response;

import com.java.assessment.reviewbox.domain.review.persistence.entity.ReviewEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReviewDetailResponse {
    private final long id;
    private final String title;
    private final String content;
    private final long likes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewDetailResponse of(
            long id, String title, String content, long likes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new ReviewDetailResponse(id, title, content, likes, createdAt, updatedAt);
    }

    public static ReviewDetailResponse from(ReviewEntity review, long likes) {
        return of(
                review.getId(),
                review.getTitle(),
                review.getContent(),
                likes,
                review.getCreatedAt(),
                review.getUpdatedAt());
    }
}
