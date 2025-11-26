package com.java.assessment.reviewbox.domain.user.presentation.response;

import com.java.assessment.reviewbox.domain.user.persistence.entity.UserEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserResponse {
    private final long id;
    private final String email;
    private final String username;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserResponse of(
            long id, String email, String username, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserResponse(id, email, username, createdAt, updatedAt);
    }

    public static UserResponse from(UserEntity user) {
        return of(user.getId(), user.getEmail(), user.getUsername(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
