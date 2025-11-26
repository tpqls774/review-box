package com.java.assessment.reviewbox.domain.auth.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginRequest {
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효하지 않은 이메일 형식입니다")
    @Size(min = 5, max = 50, message = "이메일은 5자 이상 50자 이하이어야 합니다")
    private final String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하이어야 합니다")
    private final String password;
}
