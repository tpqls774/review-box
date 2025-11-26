package com.java.assessment.reviewbox.domain.auth.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 단위 테스트")
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenStorage refreshTokenStorage;

    @Mock
    private JwtExtractor jwtExtractor;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private JwtValidator jwtValidator;

    @Mock
    private AuthenticationHolder authenticationHolder;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = UserEntity.builder()
                .email("test@example.com")
                .username("testuser")
                .password("encodedPassword123")
                .build();

        ReflectionTestUtils.setField(testUser, "id", 1L);
    }

    @Test
    @DisplayName("회원가입 성공 - 새로운 이메일로 회원가입")
    void register_Success() {
        // given
        RegisterRequest request = new RegisterRequest("newuser@example.com", "newuser", "password123");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword");

        // when
        authService.register(request);

        // then
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void register_Fail_DuplicatedEmail() {
        // given
        RegisterRequest request = new RegisterRequest("duplicate@example.com", "user", "password123");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.register(request)).isInstanceOf(DuplicatedEmailException.class);

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("로그인 성공 - 올바른 이메일과 비밀번호")
    void login_Success() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(request.getPassword(), testUser.getPassword()))
                .willReturn(true);
        given(jwtProvider.generateAccessToken(anyLong())).willReturn("accessToken");
        given(jwtProvider.generateRefreshToken(anyLong())).willReturn("refreshToken");

        // when
        LoginResponse response = authService.login(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), testUser.getPassword());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_Fail_UserNotFound() {
        // given
        LoginRequest request = new LoginRequest("notfound@example.com", "password123");
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.login(request)).isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_Fail_InvalidPassword() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "wrongPassword");
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(request.getPassword(), testUser.getPassword()))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.login(request)).isInstanceOf(InvalidCredentialsException.class);

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), testUser.getPassword());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_Success() {
        // given
        long userId = 1L;
        given(authenticationHolder.getUserId()).willReturn(userId);
        given(userRepository.existsById(userId)).willReturn(true);

        // when
        authService.logout();

        // then
        verify(authenticationHolder, times(1)).getUserId();
        verify(userRepository, times(1)).existsById(userId);
        verify(refreshTokenStorage, times(1)).deleteByUserId(userId);
    }

    @Test
    @DisplayName("로그아웃 실패 - 사용자가 존재하지 않음")
    void logout_Fail_UserNotFound() {
        // given
        long userId = 999L;
        given(authenticationHolder.getUserId()).willReturn(userId);
        given(userRepository.existsById(userId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.logout()).isInstanceOf(UserNotFoundException.class);

        verify(authenticationHolder, times(1)).getUserId();
        verify(userRepository, times(1)).existsById(userId);
        verify(refreshTokenStorage, never()).deleteByUserId(anyLong());
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_Success() {
        // given
        String refreshToken = "validRefreshToken";
        long userId = 1L;
        RefreshRequest request = new RefreshRequest(refreshToken);

        given(jwtExtractor.getPurpose(refreshToken)).willReturn(JwtPurpose.REFRESH);
        given(jwtExtractor.getSubject(refreshToken)).willReturn(userId);
        given(userRepository.existsById(userId)).willReturn(true);
        given(jwtProvider.generateAccessToken(userId)).willReturn("newAccessToken");
        given(refreshTokenStorage.findByUserId(userId)).willReturn("oldRefreshToken");
        given(jwtProvider.generateRefreshToken(userId)).willReturn("newRefreshToken");

        // when
        RefreshResponse response = authService.reissue(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("newAccessToken");
        verify(jwtValidator, times(1)).validateToken(refreshToken);
        verify(jwtExtractor, times(1)).getPurpose(refreshToken);
        verify(refreshTokenStorage, times(1)).save(userId, "newRefreshToken");
    }

    @Test
    @DisplayName("토큰 재발급 실패 - ACCESS 토큰으로 재발급 시도")
    void reissue_Fail_InvalidTokenPurpose() {
        // given
        String accessToken = "accessToken";
        RefreshRequest request = new RefreshRequest(accessToken);

        given(jwtExtractor.getPurpose(accessToken)).willReturn(JwtPurpose.ACCESS);

        // when & then
        assertThatThrownBy(() -> authService.reissue(request)).isInstanceOf(InvalidTokenException.class);

        verify(jwtValidator, times(1)).validateToken(accessToken);
        verify(jwtExtractor, times(1)).getPurpose(accessToken);
        verify(jwtProvider, never()).generateAccessToken(anyLong());
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 사용자가 존재하지 않음")
    void reissue_Fail_UserNotFound() {
        // given
        String refreshToken = "validRefreshToken";
        long userId = 999L;
        RefreshRequest request = new RefreshRequest(refreshToken);

        given(jwtExtractor.getPurpose(refreshToken)).willReturn(JwtPurpose.REFRESH);
        given(jwtExtractor.getSubject(refreshToken)).willReturn(userId);
        given(userRepository.existsById(userId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.reissue(request)).isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).existsById(userId);
        verify(jwtProvider, never()).generateAccessToken(anyLong());
    }
}
