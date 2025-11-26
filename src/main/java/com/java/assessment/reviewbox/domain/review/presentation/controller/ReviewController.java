package com.java.assessment.reviewbox.domain.review.presentation.controller;

import com.java.assessment.reviewbox.domain.review.business.service.ReviewService;
import com.java.assessment.reviewbox.domain.review.presentation.controller.docs.ReviewDocs;
import com.java.assessment.reviewbox.domain.review.presentation.request.CreateReviewRequest;
import com.java.assessment.reviewbox.domain.review.presentation.request.UpdateReviewRequest;
import com.java.assessment.reviewbox.domain.review.presentation.response.ReviewDetailResponse;
import com.java.assessment.reviewbox.domain.review.presentation.response.ReviewResponse;
import com.java.assessment.reviewbox.global.response.EmptyResponse;
import com.java.assessment.reviewbox.global.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies/{movieId}/reviews")
@RequiredArgsConstructor
public class ReviewController implements ReviewDocs {
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Override
    public EmptyResponse writeReview(@PathVariable long movieId, @Valid @RequestBody CreateReviewRequest request) {
        reviewService.createReview(movieId, request);

        return EmptyResponse.INSTANCE;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Override
    public PageResponse<ReviewResponse> getReviews(
            @PathVariable long movieId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return reviewService.getReviews(movieId, page, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{reviewId}")
    @Override
    public ReviewDetailResponse getReview(@PathVariable long movieId, @PathVariable long reviewId) {
        return reviewService.getReview(movieId, reviewId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{reviewId}")
    @Override
    public EmptyResponse updateReview(
            @PathVariable long movieId, @PathVariable long reviewId, @Valid @RequestBody UpdateReviewRequest request) {
        reviewService.updateReview(movieId, reviewId, request);

        return EmptyResponse.INSTANCE;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{reviewId}")
    @Override
    public EmptyResponse deleteReview(@PathVariable long movieId, @PathVariable long reviewId) {
        reviewService.deleteReview(movieId, reviewId);

        return EmptyResponse.INSTANCE;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{reviewId}/likes")
    @Override
    public EmptyResponse likeReview(@PathVariable long movieId, @PathVariable long reviewId) {
        reviewService.likeReview(movieId, reviewId);

        return EmptyResponse.INSTANCE;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{reviewId}/likes")
    @Override
    public EmptyResponse unlikeReview(@PathVariable long movieId, @PathVariable long reviewId) {
        reviewService.unlikeReview(movieId, reviewId);

        return EmptyResponse.INSTANCE;
    }
}
