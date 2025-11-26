package com.java.assessment.reviewbox.domain.user.presentation.controller;

import com.java.assessment.reviewbox.domain.user.business.service.UserService;
import com.java.assessment.reviewbox.domain.user.presentation.controller.docs.UserDocs;
import com.java.assessment.reviewbox.domain.user.presentation.request.UpdateUserRequest;
import com.java.assessment.reviewbox.domain.user.presentation.response.AccountResponse;
import com.java.assessment.reviewbox.domain.user.presentation.response.LikedReviewResponse;
import com.java.assessment.reviewbox.domain.user.presentation.response.MyReviewResponse;
import com.java.assessment.reviewbox.domain.user.presentation.response.UserResponse;
import com.java.assessment.reviewbox.global.response.EmptyResponse;
import com.java.assessment.reviewbox.global.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserDocs {
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    @Override
    public AccountResponse getMyAccount() {
        return userService.getMyAccount();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    @Override
    public UserResponse getUserProfile(@PathVariable long userId) {
        return userService.getUserProfile(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/me")
    @Override
    public EmptyResponse updateMyProfile(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateMyProfile(updateUserRequest);

        return EmptyResponse.INSTANCE;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/likes")
    public PageResponse<LikedReviewResponse> getMyLikedReviews(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return userService.getMyLikedReviews(page, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/reviews")
    public PageResponse<MyReviewResponse> getMyReviews(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return userService.getMyReviews(page, size);
    }
}
