package com.java.assessment.reviewbox.domain.auth.presentation.controller;

import com.java.assessment.reviewbox.domain.auth.business.service.AuthService;
import com.java.assessment.reviewbox.domain.auth.presentation.controller.docs.AuthDocs;
import com.java.assessment.reviewbox.domain.auth.presentation.request.LoginRequest;
import com.java.assessment.reviewbox.domain.auth.presentation.request.RefreshRequest;
import com.java.assessment.reviewbox.domain.auth.presentation.request.RegisterRequest;
import com.java.assessment.reviewbox.domain.auth.presentation.response.LoginResponse;
import com.java.assessment.reviewbox.domain.auth.presentation.response.RefreshResponse;
import com.java.assessment.reviewbox.global.response.EmptyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthDocs {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    @Override
    public EmptyResponse register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        return EmptyResponse.INSTANCE;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    @Override
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    @Override
    public void logout() {
        authService.logout();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    @Override
    public RefreshResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.reissue(request);
    }
}
