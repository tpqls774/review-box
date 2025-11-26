package com.java.assessment.reviewbox.domain.auth.business.service;

import com.java.assessment.reviewbox.domain.auth.business.exception.InvalidCredentialsException;
import com.java.assessment.reviewbox.domain.auth.business.exception.InvalidTokenException;
import com.java.assessment.reviewbox.domain.auth.persistence.repository.RefreshTokenStorage;
import com.java.assessment.reviewbox.domain.auth.presentation.request.LoginRequest;
import com.java.assessment.reviewbox.domain.auth.presentation.request.RefreshRequest;
import com.java.assessment.reviewbox.domain.auth.presentation.request.RegisterRequest;
import com.java.assessment.reviewbox.domain.auth.presentation.response.LoginResponse;
import com.java.assessment.reviewbox.domain.auth.presentation.response.RefreshResponse;
import com.java.assessment.reviewbox.domain.user.business.exception.DuplicatedEmailException;
import com.java.assessment.reviewbox.domain.user.business.exception.UserNotFoundException;
import com.java.assessment.reviewbox.domain.user.persistence.entity.UserEntity;
import com.java.assessment.reviewbox.domain.user.persistence.repository.UserRepository;
import com.java.assessment.reviewbox.global.security.holder.AuthenticationHolder;
import com.java.assessment.reviewbox.global.security.jwt.enums.JwtPurpose;
import com.java.assessment.reviewbox.global.security.jwt.extractor.JwtExtractor;
import com.java.assessment.reviewbox.global.security.jwt.provider.JwtProvider;
import com.java.assessment.reviewbox.global.security.jwt.validator.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenStorage refreshTokenStorage;
    private final JwtExtractor jwtExtractor;
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final AuthenticationHolder authenticationHolder;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw DuplicatedEmailException.EXCEPTION;
        }

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        UserEntity user =
                userRepository.findByEmail(request.getEmail()).orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw InvalidCredentialsException.EXCEPTION;
        }

        long userId = user.getId();
        String accessToken = jwtProvider.generateAccessToken(userId);
        String refreshToken = jwtProvider.generateRefreshToken(userId);

        refreshTokenStorage.save(userId, refreshToken);

        return LoginResponse.from(accessToken, refreshToken, user);
    }

    @Transactional(readOnly = true)
    public void logout() {
        long userId = authenticationHolder.getUserId();

        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException.EXCEPTION;
        }

        refreshTokenStorage.deleteByUserId(userId);
    }

    @Transactional(readOnly = true)
    public RefreshResponse reissue(RefreshRequest request) {
        String token = request.getRefreshToken();

        jwtValidator.validateToken(token);

        if (jwtExtractor.getPurpose(token) != JwtPurpose.REFRESH) {
            throw InvalidTokenException.EXCEPTION;
        }

        long userId = jwtExtractor.getSubject(request.getRefreshToken());

        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException.EXCEPTION;
        }

        String accessToken = jwtProvider.generateAccessToken(userId);
        String refreshToken = refreshTokenStorage.findByUserId(userId);
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);

        refreshTokenStorage.save(userId, newRefreshToken);

        return RefreshResponse.of(accessToken, refreshToken);
    }
}
