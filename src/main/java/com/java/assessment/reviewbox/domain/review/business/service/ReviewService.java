package com.java.assessment.reviewbox.domain.review.business.service;

import com.java.assessment.reviewbox.domain.movie.business.exception.MovieNotFoundException;
import com.java.assessment.reviewbox.domain.movie.persistence.entity.MovieEntity;
import com.java.assessment.reviewbox.domain.movie.persistence.enums.MovieGenre;
import com.java.assessment.reviewbox.domain.movie.persistence.repository.MovieRepository;
import com.java.assessment.reviewbox.domain.review.business.exception.AlreadyLikedException;
import com.java.assessment.reviewbox.domain.review.business.exception.InvalidReviewAccessException;
import com.java.assessment.reviewbox.domain.review.business.exception.LikeNotFoundException;
import com.java.assessment.reviewbox.domain.review.business.exception.ReviewNotFoundException;
import com.java.assessment.reviewbox.domain.review.persistence.entity.ReviewEntity;
import com.java.assessment.reviewbox.domain.review.persistence.entity.ReviewLikeEntity;
import com.java.assessment.reviewbox.domain.review.persistence.repository.ReviewLikeRepository;
import com.java.assessment.reviewbox.domain.review.persistence.repository.ReviewRepository;
import com.java.assessment.reviewbox.domain.review.presentation.request.CreateReviewRequest;
import com.java.assessment.reviewbox.domain.review.presentation.request.UpdateReviewRequest;
import com.java.assessment.reviewbox.domain.review.presentation.response.ReviewDetailResponse;
import com.java.assessment.reviewbox.domain.review.presentation.response.ReviewResponse;
import com.java.assessment.reviewbox.domain.user.business.exception.UserNotFoundException;
import com.java.assessment.reviewbox.domain.user.persistence.entity.UserEntity;
import com.java.assessment.reviewbox.domain.user.persistence.repository.UserRepository;
import com.java.assessment.reviewbox.global.response.PageResponse;
import com.java.assessment.reviewbox.global.security.holder.AuthenticationHolder;
import com.java.assessment.reviewbox.global.tmdb.client.TmdbClient;
import com.java.assessment.reviewbox.global.tmdb.constants.TmdbConstants;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbCreditResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbMovieDetailResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final AuthenticationHolder authenticationHolder;
    private final TmdbClient tmdbClient;

    @Transactional
    public void createReview(long movieId, CreateReviewRequest request) {
        MovieEntity movie = movieRepository.findById(movieId).orElseGet(() -> fetchAndSaveMovie(movieId));
        long userId = authenticationHolder.getUserId();

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);

        ReviewEntity review = ReviewEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .movie(movie)
                .user(user)
                .build();

        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getReviews(long movieId, int page, int size) {
        MovieEntity movie = movieRepository.findById(movieId).orElseThrow(() -> MovieNotFoundException.EXCEPTION);

        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<ReviewEntity> reviewPage = reviewRepository.findByMovieIdWithUser(movie.getId(), pageable);

        if (reviewPage.isEmpty()) {
            return PageResponse.of(page, size, 0, false, List.of());
        }

        List<Long> reviewIds = reviewPage.stream().map(ReviewEntity::getId).toList();

        Map<Long, Long> likesMap = reviewLikeRepository.countLikesGroupByReviewIds(reviewIds).stream()
                .collect(Collectors.toMap(
                        ReviewLikeRepository.ReviewLikesCount::getReviewId,
                        ReviewLikeRepository.ReviewLikesCount::getLikes));

        List<ReviewResponse> content = reviewPage.stream()
                .map(review -> ReviewResponse.of(
                        review.getId(),
                        review.getTitle(),
                        likesMap.getOrDefault(review.getId(), 0L),
                        ReviewResponse.Writer.of(
                                review.getUser().getId(), review.getUser().getUsername()),
                        review.getCreatedAt(),
                        review.getUpdatedAt()))
                .toList();

        return PageResponse.of(
                reviewPage.getNumber() + 1,
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.hasNext(),
                content);
    }

    @Transactional(readOnly = true)
    public ReviewDetailResponse getReview(long movieId, long reviewId) {
        MovieEntity movie = movieRepository.findById(movieId).orElseThrow(() -> MovieNotFoundException.EXCEPTION);
        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        if (!review.getMovie().getId().equals(movie.getId())) {
            throw InvalidReviewAccessException.EXCEPTION;
        }

        long likes = reviewLikeRepository.countByReviewId(review.getId());

        return ReviewDetailResponse.from(review, likes);
    }

    @Transactional
    public void updateReview(long movieId, long reviewId, UpdateReviewRequest request) {
        MovieEntity movie = movieRepository.findById(movieId).orElseThrow(() -> MovieNotFoundException.EXCEPTION);
        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        if (!review.getMovie().getId().equals(movie.getId())) {
            throw InvalidReviewAccessException.EXCEPTION;
        }

        review.changeTitle(request.getTitle());
        review.changeContent(request.getContent());
    }

    @Transactional
    public void deleteReview(long movieId, long reviewId) {
        long userId = authenticationHolder.getUserId();

        MovieEntity movie = movieRepository.findById(movieId).orElseThrow(() -> MovieNotFoundException.EXCEPTION);

        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        if (!review.getMovie().getId().equals(movie.getId())) {
            throw InvalidReviewAccessException.EXCEPTION;
        }

        if (!review.getUser().getId().equals(userId)) {
            throw InvalidReviewAccessException.EXCEPTION;
        }

        reviewLikeRepository.deleteByReviewId(reviewId);
        reviewRepository.delete(review);
    }

    @Transactional
    public void likeReview(long movieId, long reviewId) {
        MovieEntity movie = movieRepository.findById(movieId).orElseThrow(() -> MovieNotFoundException.EXCEPTION);
        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow(() -> ReviewNotFoundException.EXCEPTION);
        long userId = authenticationHolder.getUserId();

        if (!review.getMovie().getId().equals(movie.getId())) {
            throw InvalidReviewAccessException.EXCEPTION;
        }

        if (!review.getUser().getId().equals(userId)) {
            throw InvalidReviewAccessException.EXCEPTION;
        }

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw AlreadyLikedException.EXCEPTION;
        }

        ReviewLikeEntity like =
                ReviewLikeEntity.builder().user(user).review(review).build();

        reviewLikeRepository.save(like);
    }

    @Transactional
    public void unlikeReview(long movieId, long reviewId) {
        MovieEntity movie = movieRepository.findById(movieId).orElseThrow(() -> MovieNotFoundException.EXCEPTION);

        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        if (!review.getMovie().getId().equals(movie.getId())) {
            throw InvalidReviewAccessException.EXCEPTION;
        }

        long userId = authenticationHolder.getUserId();

        ReviewLikeEntity like = reviewLikeRepository
                .findByUserIdAndReviewId(userId, reviewId)
                .orElseThrow(() -> LikeNotFoundException.EXCEPTION);

        reviewLikeRepository.delete(like);
    }

    private MovieEntity fetchAndSaveMovie(long movieId) {
        TmdbMovieDetailResponse detail = tmdbClient.getMovieDetail(movieId);
        TmdbCreditResponse credits = tmdbClient.getMovieCredits(movieId);

        String director = extractDirector(credits);

        Set<MovieGenre> genres = detail.getGenres().stream()
                .map(g -> MovieGenre.fromTmdbId(g.getId()))
                .collect(Collectors.toSet());

        MovieEntity movie = MovieEntity.builder()
                .id(detail.getId())
                .title(detail.getTitle())
                .year(Integer.parseInt(detail.getReleaseDate().substring(0, 4)))
                .posterUrl(TmdbConstants.TMDB_IMAGE_BASE_URL + detail.getPosterPath())
                .summary(detail.getOverview())
                .rating(detail.getVoteAverage())
                .runtime(detail.getRuntime())
                .genres(genres)
                .director(director)
                .build();

        return movieRepository.save(movie);
    }

    private String extractDirector(TmdbCreditResponse credits) {
        return credits.getCrew().stream()
                .filter(c -> "Director".equalsIgnoreCase(c.getJob()))
                .map(TmdbCreditResponse.Crew::getName)
                .findFirst()
                .orElse("Unknown");
    }
}
