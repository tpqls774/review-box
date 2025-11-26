package com.java.assessment.reviewbox.domain.auth.presentation.controller.docs;

import com.java.assessment.reviewbox.domain.auth.presentation.request.LoginRequest;
import com.java.assessment.reviewbox.domain.auth.presentation.request.RefreshRequest;
import com.java.assessment.reviewbox.domain.auth.presentation.request.RegisterRequest;
import com.java.assessment.reviewbox.domain.auth.presentation.response.LoginResponse;
import com.java.assessment.reviewbox.domain.auth.presentation.response.RefreshResponse;
import com.java.assessment.reviewbox.global.response.EmptyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthDocs {
    @Operation(
            summary = "회원가입",
            description = "사용자 정보를 기반으로 회원가입을 수행합니다.",
            requestBody =
                    @RequestBody(
                            required = true,
                            content =
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                @ExampleObject(
                                                        name = "회원가입 성공 요청",
                                                        description = "올바른 회원가입 요청 예시",
                                                        value =
                                                                """
                                                            {
                                                              "email": "user@example.com",
                                                              "username": "sehyuk",
                                                              "password": "qwer1234!"
                                                            }
                                                            """),
                                                @ExampleObject(
                                                        name = "회원가입 실패 요청",
                                                        description = "유효성 검증 실패 예시",
                                                        value =
                                                                """
                                                            {
                                                              "email": "not-an-email",
                                                              "username": "",
                                                              "password": "123"
                                                            }
                                                            """)
                                            })),
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "회원가입 성공",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 201,
                                                              "data": null,
                                                              "message": "Created"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "400",
                        description = "유효성 검증 실패",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 400,
                                                              "code": "VALIDATION_ERROR",
                                                              "message": "유효하지 않은 이메일 형식입니다"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "409",
                        description = "이미 존재하는 이메일",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 409,
                                                              "code": "DUPLICATED_EMAIL",
                                                              "message": "이미 사용 중인 이메일입니다"
                                                            }
                                                            """)))
            })
    EmptyResponse register(RegisterRequest request);

    @Operation(
            summary = "로그인",
            description = "이메일/비밀번호로 로그인하여 AccessToken, RefreshToken을 발급합니다.",
            requestBody =
                    @RequestBody(
                            required = true,
                            content =
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                @ExampleObject(
                                                        name = "로그인 성공 요청",
                                                        value =
                                                                """
                                                            {
                                                              "email": "user@example.com",
                                                              "password": "qwer1234!"
                                                            }
                                                            """),
                                                @ExampleObject(
                                                        name = "로그인 실패 요청",
                                                        description = "비밀번호가 틀린 경우",
                                                        value =
                                                                """
                                                            {
                                                              "email": "user@example.com",
                                                              "password": "wrongpassword"
                                                            }
                                                            """)
                                            })),
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "로그인 성공",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 200,
                                                              "data": {
                                                                "accessToken": "eyJh...abc",
                                                                "refreshToken": "eyJh...xyz",
                                                                "user": {
                                                                  "id": 1,
                                                                  "email": "user@example.com",
                                                                  "username": "sehyuk"
                                                                }
                                                              },
                                                              "message": "OK"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "400",
                        description = "유효성 검증 실패",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 400,
                                                              "code": "VALIDATION_ERROR",
                                                              "message": "유효하지 않은 이메일 형식입니다"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "401",
                        description = "잘못된 자격 증명",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 401,
                                                              "code": "INVALID_CREDENTIALS",
                                                              "message": "이메일 또는 비밀번호가 올바르지 않습니다"
                                                            }
                                                            """)))
            })
    LoginResponse login(LoginRequest request);

    @Operation(
            summary = "로그아웃",
            description = "저장된 RefreshToken을 삭제하여 로그아웃을 수행합니다.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "로그아웃 성공",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 200,
                                                              "data": null,
                                                              "message": "OK"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "401",
                        description = "인증 실패",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 401,
                                                              "code": "AUTHENTICATION_FAILED",
                                                              "message": "인증이 필요합니다"
                                                            }
                                                            """)))
            })
    void logout();

    @Operation(
            summary = "토큰 재발급",
            description = "유효한 RefreshToken을 사용해 새로운 AccessToken 및 RefreshToken을 발급합니다.",
            requestBody =
                    @RequestBody(
                            content =
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                @ExampleObject(
                                                        name = "재발급 성공 요청",
                                                        value =
                                                                """
                                                            {
                                                              "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                                            }
                                                            """),
                                                @ExampleObject(
                                                        name = "재발급 실패 요청",
                                                        description = "refreshToken 포맷이 비정상",
                                                        value =
                                                                """
                                                            {
                                                              "refreshToken": "invalid-token"
                                                            }
                                                            """)
                                            })),
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "토큰 재발급 성공",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 200,
                                                              "data": {
                                                                "accessToken": "eyJh...new",
                                                                "refreshToken": "eyJh...newRefresh"
                                                              },
                                                              "message": "OK"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "400",
                        description = "유효성 검증 실패",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 400,
                                                              "code": "VALIDATION_ERROR",
                                                              "message": "리프레시 토큰은 필수입니다"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "401",
                        description = "RefreshToken이 유효하지 않음",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 401,
                                                              "code": "INVALID_TOKEN",
                                                              "message": "유효하지 않거나 만료된 리프레시 토큰입니다"
                                                            }
                                                            """)))
            })
    RefreshResponse refresh(RefreshRequest request);
}
