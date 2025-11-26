package com.java.assessment.reviewbox.domain.user.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.java.assessment.reviewbox.domain.user.business.exception.UserNotFoundException;
import com.java.assessment.reviewbox.domain.user.persistence.entity.UserEntity;
import com.java.assessment.reviewbox.domain.user.persistence.repository.UserRepository;
import com.java.assessment.reviewbox.domain.user.presentation.request.UpdateUserRequest;
import com.java.assessment.reviewbox.domain.user.presentation.response.AccountResponse;
import com.java.assessment.reviewbox.domain.user.presentation.response.UserResponse;
import com.java.assessment.reviewbox.global.security.holder.AuthenticationHolder;
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
@DisplayName("UserService 단위 테스트")
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationHolder authenticationHolder;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        UserEntity actualUser = UserEntity.builder()
                .email("test@example.com")
                .username("testuser")
                .password("encodedPassword123")
                .build();

        ReflectionTestUtils.setField(actualUser, "id", 1L);

        this.testUser = spy(actualUser);
    }

    @Test
    @DisplayName("내 계정 조회 성공")
    void getMyAccount_Success() {
        // given
        long userId = 1L;
        given(authenticationHolder.getUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));

        // when
        AccountResponse result = userService.getMyAccount();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(authenticationHolder, times(1)).getUserId();
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("내 계정 조회 실패 - 사용자가 존재하지 않음")
    void getMyAccount_Fail_UserNotFound() {
        // given
        long userId = 999L;
        given(authenticationHolder.getUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getMyAccount()).isInstanceOf(UserNotFoundException.class);

        verify(authenticationHolder, times(1)).getUserId();
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("사용자 프로필 조회 성공")
    void getUserProfile_Success() {
        // given
        long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));

        // when
        UserResponse result = userService.getUserProfile(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("사용자 프로필 조회 실패 - 사용자가 존재하지 않음")
    void getUserProfile_Fail_UserNotFound() {
        // given
        long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserProfile(userId)).isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("내 프로필 수정 성공")
    void updateMyProfile_Success() {
        // given
        long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("newemail@example.com", "newusername", "newPassword123");

        given(authenticationHolder.getUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedNewPassword");

        // when
        userService.updateMyProfile(request);

        // then
        verify(authenticationHolder, times(1)).getUserId();
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode(request.getPassword());

        verify(testUser).changeEmail("newemail@example.com");
        verify(testUser).changeUsername("newusername");
        verify(testUser).changePassword("encodedNewPassword");
    }

    @Test
    @DisplayName("내 프로필 수정 실패 - 사용자가 존재하지 않음")
    void updateMyProfile_Fail_UserNotFound() {
        // given
        long userId = 999L;
        UpdateUserRequest request = new UpdateUserRequest("newemail@example.com", "newusername", "newPassword123");

        given(authenticationHolder.getUserId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.updateMyProfile(request)).isInstanceOf(UserNotFoundException.class);

        verify(authenticationHolder, times(1)).getUserId();
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, never()).encode(anyString());
    }
}
