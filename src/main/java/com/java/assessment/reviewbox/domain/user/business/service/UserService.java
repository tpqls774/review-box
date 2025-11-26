package com.java.assessment.reviewbox.domain.user.business.service;

import com.java.assessment.reviewbox.domain.review.persistence.entity.ReviewEntity;
import com.java.assessment.reviewbox.domain.review.persistence.entity.ReviewLikeEntity;
import com.java.assessment.reviewbox.domain.review.persistence.repository.ReviewLikeRepository;
import com.java.assessment.reviewbox.domain.review.persistence.repository.ReviewRepository;
import com.java.assessment.reviewbox.domain.user.business.exception.UserNotFoundException;
import com.java.assessment.reviewbox.domain.user.persistence.entity.UserEntity;
import com.java.assessment.reviewbox.domain.user.persistence.repository.UserRepository;
import com.java.assessment.reviewbox.domain.user.presentation.request.UpdateUserRequest;
import com.java.assessment.reviewbox.domain.user.presentation.response.AccountResponse;
import com.java.assessment.reviewbox.domain.user.presentation.response.LikedReviewResponse;
import com.java.assessment.reviewbox.domain.user.presentation.response.MyReviewResponse;
import com.java.assessment.reviewbox.domain.user.presentation.response.UserResponse;
import com.java.assessment.reviewbox.global.response.PageResponse;
import com.java.assessment.reviewbox.global.security.holder.AuthenticationHolder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final AuthenticationHolder authenticationHolder;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AccountResponse getMyAccount() {
        long userId = authenticationHolder.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);

        return AccountResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserProfile(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);

        return UserResponse.from(user);
    }

    @Transactional
    public void updateMyProfile(UpdateUserRequest request) {
        long userId = authenticationHolder.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);
        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();

        user.changeEmail(email);
        user.changeUsername(username);
        user.changePassword(passwordEncoder.encode(password));
    }

    @Transactional(readOnly = true)
    public PageResponse<LikedReviewResponse> getMyLikedReviews(int page, int size) {
        long userId = authenticationHolder.getUserId();

        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<ReviewLikeEntity> likePage = reviewLikeRepository.findByUserId(userId, pageable);

        if (likePage.isEmpty()) {
            return PageResponse.of(page, size, 0, false, List.of());
        }

        List<Long> reviewIds = likePage.stream()
                .map(ReviewLikeEntity::getReview)
                .map(ReviewEntity::getId)
                .toList();

        Map<Long, ReviewEntity> reviewMap =
                reviewRepository.findByIdIn(reviewIds).stream().collect(Collectors.toMap(ReviewEntity::getId, r -> r));

        Map<Long, Long> likeCountMap = reviewLikeRepository.countLikesGroupByReviewIds(reviewIds).stream()
                .collect(Collectors.toMap(
                        ReviewLikeRepository.ReviewLikesCount::getReviewId,
                        ReviewLikeRepository.ReviewLikesCount::getLikes));

        List<LikedReviewResponse> content = likePage.stream()
                .map(like -> {
                    ReviewEntity review = reviewMap.get(like.getReview().getId());
                    long likeCount = likeCountMap.getOrDefault(like.getReview().getId(), 0L);

                    return LikedReviewResponse.of(
                            review.getId(),
                            review.getMovie().getId(),
                            review.getMovie().getTitle(),
                            review.getTitle(),
                            createPreview(review.getContent()),
                            likeCount,
                            review.getCreatedAt().toString(),
                            review.getUser().getId(),
                            review.getUser().getUsername());
                })
                .toList();

        return PageResponse.of(
                likePage.getNumber() + 1, likePage.getSize(), likePage.getTotalElements(), likePage.hasNext(), content);
    }

    @Transactional(readOnly = true)
    public PageResponse<MyReviewResponse> getMyReviews(int page, int size) {
        long userId = authenticationHolder.getUserId();
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<ReviewEntity> reviewPage = reviewRepository.findByUserId(userId, pageable);

        if (reviewPage.isEmpty()) {
            return PageResponse.of(page, size, 0, false, List.of());
        }

        List<Long> reviewIds = reviewPage.stream().map(ReviewEntity::getId).toList();

        Map<Long, Long> likeCountMap = reviewLikeRepository.countLikesGroupByReviewIds(reviewIds).stream()
                .collect(Collectors.toMap(
                        ReviewLikeRepository.ReviewLikesCount::getReviewId,
                        ReviewLikeRepository.ReviewLikesCount::getLikes));

        List<MyReviewResponse> content = reviewPage.stream()
                .map(review -> MyReviewResponse.of(
                        review.getId(),
                        review.getMovie().getId(),
                        review.getMovie().getTitle(),
                        review.getTitle(),
                        createPreview(review.getContent()),
                        likeCountMap.getOrDefault(review.getId(), 0L),
                        review.getCreatedAt().toString()))
                .toList();

        return PageResponse.of(
                reviewPage.getNumber() + 1,
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.hasNext(),
                content);
    }

    private String createPreview(String content) {
        if (content == null) return "";

        return content.length() <= 50 ? content : content.substring(0, 50) + "...";
    }
}
