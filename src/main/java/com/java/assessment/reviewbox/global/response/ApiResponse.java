package com.java.assessment.reviewbox.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiResponse<T> {
    private final int status;
    private final T data;
    private final String message;

    public static <T> ApiResponse<T> of(int status, T data, String message) {
        return new ApiResponse<>(status, data, message);
    }
}
