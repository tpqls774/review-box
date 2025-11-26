package com.java.assessment.reviewbox.domain.user.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpdateUserRequest {
    @Email(message = "유효하지 않은 이메일 형식입니다")
    @Size(min = 5, max = 50, message = "이메일은 5자 이상 50자 이하이어야 합니다")
    private final String email;

    @Size(min = 2, max = 20, message = "사용자명은 2자 이상 20자 이하이어야 합니다")
    private final String username;

    @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하이어야 합니다")
    private final String password;
}
