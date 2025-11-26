package com.java.assessment.reviewbox.domain.user.presentation.controller.docs;

import com.java.assessment.reviewbox.domain.user.presentation.request.UpdateUserRequest;
import com.java.assessment.reviewbox.domain.user.presentation.response.AccountResponse;
import com.java.assessment.reviewbox.domain.user.presentation.response.UserResponse;
import com.java.assessment.reviewbox.global.response.EmptyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "유저 프로필 및 계정 관련 API")
public interface UserDocs {
    @Operation(
            summary = "내 계정 정보 조회",
            description = "로그인한 사용자의 계정 정보를 반환합니다.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "성공",
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
                                                                "id": 1,
                                                                "email": "me@example.com",
                                                                "username": "sehyuk",
                                                                "createdAt": "2025-01-20T12:00:00",
                                                                "updatedAt": "2025-01-21T14:30:00"
                                                              },
                                                              "message": "OK"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "401",
                        description = "인증 실패",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 401,
                                                              "code": "AUTHENTICATION_FAILED",
                                                              "message": "인증에 실패했습니다"
                                                            }
                                                            """)))
            })
    AccountResponse getMyAccount();

    @Operation(
            summary = "사용자 프로필 조회",
            description = "특정 사용자의 프로필을 조회합니다.",
            parameters = {@Parameter(name = "userId", description = "프로필 조회할 사용자 ID", example = "1")},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "성공",
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
                                                                "id": 1,
                                                                "email": "me@example.com",
                                                                "username": "sehyuk",
                                                                "createdAt": "2025-01-20T12:00:00",
                                                                "updatedAt": "2025-01-21T14:30:00"
                                                              },
                                                              "message": "OK"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "401",
                        description = "인증 실패",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                {
                                                  "status": 401,
                                                  "code": "AUTHENTICATION_FAILED",
                                                  "message": "인증에 실패했습니다"
                                                }
                                                """))),
                @ApiResponse(
                        responseCode = "404",
                        description = "해당 사용자를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 404,
                                                              "code": "USER_NOT_FOUND",
                                                              "message": "사용자를 찾을 수 없습니다"
                                                            }
                                                            """)))
            })
    UserResponse getUserProfile(long userId);

    @Operation(
            summary = "내 프로필 수정",
            description =
                    """
                            로그인한 사용자의 프로필 정보를 수정합니다.<br><br>
                            - 필드를 **보내지 않으면 수정하지 않음**<br>
                            - 필드를 `null`로 보내면 해당 값을 **null로 업데이트**<br>
                            - 값을 보내면 **해당 값으로 업데이트**<br>
                            """,
            requestBody =
                    @RequestBody(
                            content =
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                @ExampleObject(
                                                        name = "수정 성공 요청 예시",
                                                        value =
                                                                """
                                                            {
                                                              "email": "new_email@example.com",
                                                              "username": "new_name",
                                                              "password": "newpassword123"
                                                            }
                                                            """),
                                                @ExampleObject(
                                                        name = "일부 필드만 수정",
                                                        value =
                                                                """
                                                            {
                                                              "username": "only_changed"
                                                            }
                                                            """),
                                                @ExampleObject(
                                                        name = "유효성 검증 실패 예시",
                                                        value =
                                                                """
                                                            {
                                                              "email": "invalid-email",
                                                              "password": "123"
                                                            }
                                                            """)
                                            })),
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "수정 성공",
                        content =
                                @Content(
                                        mediaType = "application/json",
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
                        responseCode = "401",
                        description = "인증 실패",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 401,
                                                              "code": "AUTHENTICATION_FAILED",
                                                              "message": "인증에 실패했습니다"
                                                            }
                                                            """)))
            })
    EmptyResponse updateMyProfile(UpdateUserRequest updateUserRequest);
}
