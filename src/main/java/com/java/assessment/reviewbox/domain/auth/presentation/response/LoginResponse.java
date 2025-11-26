package com.java.assessment.reviewbox.domain.auth.presentation.response;

import com.java.assessment.reviewbox.domain.user.persistence.entity.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
    private final User user;

    public static LoginResponse of(String accessToken, String refreshToken, User user) {
        return new LoginResponse(accessToken, refreshToken, user);
    }

    public static LoginResponse from(String accessToken, String refreshToken, UserEntity user) {
        return of(accessToken, refreshToken, User.from(user));
    }

    @RequiredArgsConstructor
    @Getter
    public static class User {
        private final long id;
        private final String email;
        private final String username;

        public static User of(long id, String email, String username) {
            return new User(id, email, username);
        }

        public static User from(UserEntity user) {
            return of(user.getId(), user.getEmail(), user.getUsername());
        }
    }
}
