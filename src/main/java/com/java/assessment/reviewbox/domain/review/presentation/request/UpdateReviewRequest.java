package com.java.assessment.reviewbox.domain.review.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpdateReviewRequest {
    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하이어야 합니다")
    private final String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 10, max = 3000, message = "내용은 10자 이상 3000자 이하이어야 합니다")
    private final String content;
}
