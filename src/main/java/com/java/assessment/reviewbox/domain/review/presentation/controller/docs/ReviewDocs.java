package com.java.assessment.reviewbox.domain.review.presentation.controller.docs;

import com.java.assessment.reviewbox.domain.review.presentation.request.CreateReviewRequest;
import com.java.assessment.reviewbox.domain.review.presentation.request.UpdateReviewRequest;
import com.java.assessment.reviewbox.domain.review.presentation.response.ReviewDetailResponse;
import com.java.assessment.reviewbox.domain.review.presentation.response.ReviewResponse;
import com.java.assessment.reviewbox.global.response.EmptyResponse;
import com.java.assessment.reviewbox.global.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Review", description = "리뷰 관련 API")
public interface ReviewDocs {
    @Operation(
            summary = "리뷰 작성",
            description = "특정 영화에 대한 리뷰를 작성합니다.",
            parameters = {@Parameter(name = "movieId", description = "영화 ID", example = "1116465")},
            requestBody =
                    @RequestBody(
                            required = true,
                            content =
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                @ExampleObject(
                                                        name = "리뷰 작성 성공 요청",
                                                        description = "올바른 리뷰 작성 요청 예시",
                                                        value =
                                                                """
                                                            {
                                                              "title": "정말 재미있는 영화였습니다!",
                                                              "content": "스토리도 좋고 연기도 훌륭했습니다. 강력히 추천합니다."
                                                            }
                                                            """),
                                                @ExampleObject(
                                                        name = "리뷰 작성 실패 요청",
                                                        description = "유효성 검증 실패 예시 (내용이 너무 짧음)",
                                                        value =
                                                                """
                                                            {
                                                              "title": "",
                                                              "content": "짧음"
                                                            }
                                                            """)
                                            })),
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "리뷰 작성 성공",
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
                                                              "message": "내용은 10자 이상 3000자 이하이어야 합니다"
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
                        description = "영화를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 404,
                                                              "code": "MOVIE_NOT_FOUND",
                                                              "message": "영화를 찾을 수 없습니다"
                                                            }
                                                            """)))
            })
    EmptyResponse writeReview(long movieId, CreateReviewRequest request);

    @Operation(
            summary = "리뷰 목록 조회",
            description = "특정 영화의 리뷰 목록을 페이지 단위로 조회합니다.",
            parameters = {
                @Parameter(name = "movieId", description = "영화 ID", example = "1116465"),
                @Parameter(name = "page", description = "페이지 번호 (1부터 시작)", example = "1"),
                @Parameter(name = "size", description = "페이지 크기", example = "20")
            },
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "리뷰 목록 조회 성공",
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
                                                                "content": [
                                                                  {
                                                                    "id": 1,
                                                                    "title": "정말 재미있는 영화였습니다!",
                                                                    "content": "스토리도 좋고 연기도 훌륭했습니다.",
                                                                    "username": "sehyuk",
                                                                    "likeCount": 15,
                                                                    "createdAt": "2025-01-20T12:00:00"
                                                                  }
                                                                ],
                                                                "page": 0,
                                                                "size": 20,
                                                                "totalElements": 1,
                                                                "totalPages": 1
                                                              },
                                                              "message": "OK"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "404",
                        description = "영화를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 404,
                                                              "code": "MOVIE_NOT_FOUND",
                                                              "message": "영화를 찾을 수 없습니다"
                                                            }
                                                            """)))
            })
    PageResponse<ReviewResponse> getReviews(long movieId, int page, int size);

    @Operation(
            summary = "리뷰 상세 조회",
            description = "특정 리뷰의 상세 정보를 조회합니다.",
            parameters = {
                @Parameter(name = "movieId", description = "영화 ID", example = "1116465"),
                @Parameter(name = "reviewId", description = "리뷰 ID", example = "1")
            },
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "리뷰 조회 성공",
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
                                                                "title": "정말 재미있는 영화였습니다!",
                                                                "content": "스토리도 좋고 연기도 훌륭했습니다. 강력히 추천합니다.",
                                                                "username": "sehyuk",
                                                                "likeCount": 15,
                                                                "isLiked": true,
                                                                "createdAt": "2025-01-20T12:00:00",
                                                                "updatedAt": "2025-01-21T14:30:00"
                                                              },
                                                              "message": "OK"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "400",
                        description = "해당 영화의 리뷰가 아님",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 400,
                                                              "code": "INVALID_REVIEW_ACCESS",
                                                              "message": "해당 영화의 리뷰가 아닙니다"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "404",
                        description = "리뷰를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 404,
                                                              "code": "REVIEW_NOT_FOUND",
                                                              "message": "리뷰를 찾을 수 없습니다"
                                                            }
                                                            """)))
            })
    ReviewDetailResponse getReview(long movieId, long reviewId);

    @Operation(
            summary = "리뷰 수정",
            description = "자신이 작성한 리뷰를 수정합니다.",
            parameters = {
                @Parameter(name = "movieId", description = "영화 ID", example = "1116465"),
                @Parameter(name = "reviewId", description = "리뷰 ID", example = "1")
            },
            requestBody =
                    @RequestBody(
                            required = true,
                            content =
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                @ExampleObject(
                                                        name = "리뷰 수정 성공 요청",
                                                        value =
                                                                """
                                                            {
                                                              "title": "수정된 제목입니다",
                                                              "content": "수정된 내용입니다. 다시 보니 더 좋았습니다."
                                                            }
                                                            """),
                                                @ExampleObject(
                                                        name = "리뷰 수정 실패 요청",
                                                        description = "내용이 너무 짧음",
                                                        value =
                                                                """
                                                            {
                                                              "title": "제목",
                                                              "content": "짧음"
                                                            }
                                                            """)
                                            })),
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "리뷰 수정 성공",
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
                                                              "message": "내용은 10자 이상 3000자 이하이어야 합니다"
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
                        description = "리뷰를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 404,
                                                              "code": "REVIEW_NOT_FOUND",
                                                              "message": "리뷰를 찾을 수 없습니다"
                                                            }
                                                            """)))
            })
    EmptyResponse updateReview(long movieId, long reviewId, UpdateReviewRequest request);

    @Operation(
            summary = "리뷰 삭제",
            description = "자신이 작성한 리뷰를 삭제합니다.",
            parameters = {
                @Parameter(name = "movieId", description = "영화 ID", example = "1116465"),
                @Parameter(name = "reviewId", description = "리뷰 ID", example = "1")
            },
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "리뷰 삭제 성공",
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
                        description = "리뷰를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 404,
                                                              "code": "REVIEW_NOT_FOUND",
                                                              "message": "리뷰를 찾을 수 없습니다"
                                                            }
                                                            """)))
            })
    EmptyResponse deleteReview(long movieId, long reviewId);

    @Operation(
            summary = "리뷰 좋아요",
            description = "특정 리뷰에 좋아요를 추가합니다.",
            parameters = {
                @Parameter(name = "movieId", description = "영화 ID", example = "1116465"),
                @Parameter(name = "reviewId", description = "리뷰 ID", example = "1")
            },
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "좋아요 성공",
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
                        description = "이미 좋아요를 누른 리뷰",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 400,
                                                              "code": "ALREADY_LIKED",
                                                              "message": "이미 좋아요를 누른 리뷰입니다"
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
                        description = "리뷰를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 404,
                                                              "code": "REVIEW_NOT_FOUND",
                                                              "message": "리뷰를 찾을 수 없습니다"
                                                            }
                                                            """)))
            })
    EmptyResponse likeReview(long movieId, long reviewId);

    @Operation(
            summary = "리뷰 좋아요 취소",
            description = "특정 리뷰의 좋아요를 취소합니다.",
            parameters = {
                @Parameter(name = "movieId", description = "영화 ID", example = "1116465"),
                @Parameter(name = "reviewId", description = "리뷰 ID", example = "1")
            },
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "좋아요 취소 성공",
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
                        description = "좋아요를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                """
                                                            {
                                                              "status": 404,
                                                              "code": "LIKE_NOT_FOUND",
                                                              "message": "좋아요를 찾을 수 없습니다"
                                                            }
                                                            """)))
            })
    EmptyResponse unlikeReview(long movieId, long reviewId);
}
