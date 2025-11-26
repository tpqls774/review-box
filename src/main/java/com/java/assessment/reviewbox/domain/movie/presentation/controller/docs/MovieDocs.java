package com.java.assessment.reviewbox.domain.movie.presentation.controller.docs;

import com.java.assessment.reviewbox.domain.movie.business.enums.MovieCategory;
import com.java.assessment.reviewbox.domain.movie.presentation.response.MovieDetailResponse;
import com.java.assessment.reviewbox.domain.movie.presentation.response.MovieResponse;
import com.java.assessment.reviewbox.global.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Movie", description = "영화 관련 API")
public interface MovieDocs {
    @Operation(
            summary = "영화 목록 조회",
            description = "카테고리별 영화 목록을 페이지 단위로 조회합니다.",
            parameters = {
                @Parameter(
                        name = "category",
                        description = "영화 카테고리 (popular: 인기, now_playing: 현재 상영중, upcoming: 개봉 예정, top_rated: 높은 평점)",
                        example = "popular"),
                @Parameter(name = "page", description = "페이지 번호 (1부터 시작)", example = "1")
            },
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "영화 목록 조회 성공",
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
                                                                "page": 1,
                                                                "size": 20,
                                                                "totalCount": 100,
                                                                "hasNext": true,
                                                                "content": [
                                                                  {
                                                                    "id": 157336,
                                                                    "title": "인터스텔라",
                                                                    "year": 2014,
                                                                    "posterUrl": "/poster.jpg",
                                                                    "overview": "세계 각국의 정부와 경제가 완전히 붕괴된 미래가 다가온다...",
                                                                    "rating": 8.442,
                                                                    "genres": ["SCIENCE_FICTION", "DRAMA", "ADVENTURE"]
                                                                  }
                                                                ]
                                                              },
                                                              "message": "OK"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "400",
                        description = "잘못된 카테고리",
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
                                                              "message": "유효하지 않은 카테고리입니다"
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
    PageResponse<MovieResponse> getMovies(MovieCategory category, int page, int size);

    @Operation(
            summary = "영화 상세 조회",
            description = "특정 영화의 상세 정보를 조회합니다.",
            parameters = {@Parameter(name = "movieId", description = "영화 ID", example = "1116465")},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "영화 상세 조회 성공",
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
                                                                "id": 1116465,
                                                                "title": "성룡의 전설",
                                                                "year": 2024,
                                                                "posterUrl": "/poster.jpg",
                                                                "overview": "고고학자 첸은 학생들과 발굴 작업 중, 옥으로 만들어진 신비한 목걸이를 발견한다. 때는 한나라, 흉노 족에게 쫓기던 공주 멍윈을 구해준 장군 조전은 나라를 지키기 위한 그들과의 전쟁을 준비한다. 시간은 다시 현재, 목걸이가 꿈속에서 본 과거와 현실을 연결시켜준다는 것을 깨달은 첸은 피할 수 없는 운명의 소용돌이에 휩싸이는데…",
                                                                "rating": 6.692,
                                                                "runtime": 129,
                                                                "genres": ["ACTION", "FANTASY", "ADVENTURE"],
                                                                "director": "당계례",
                                                                "cast": [
                                                                  {
                                                                    "id": 18897,
                                                                    "name": "성룡",
                                                                    "character": "Professor Fang/Zhao Zhan",
                                                                    "profileUrl": "/profile.jpg"
                                                                  }
                                                                ]
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
    MovieDetailResponse getMovie(long movieId);

    @Operation(
            summary = "영화 검색",
            description = "키워드로 영화를 검색합니다.",
            parameters = {
                @Parameter(name = "query", description = "검색 키워드", example = "성룡"),
                @Parameter(name = "page", description = "페이지 번호 (1부터 시작)", example = "1")
            },
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "영화 검색 성공",
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
                                                                "page": 1,
                                                                "size": 17,
                                                                "totalCount": 17,
                                                                "hasNext": false,
                                                                "content": [
                                                                  {
                                                                    "id": 1116465,
                                                                    "title": "성룡의 전설",
                                                                    "year": 2024,
                                                                    "posterUrl": "/poster1.jpg",
                                                                    "overview": "고고학자 첸은 학생들과 발굴 작업 중, 옥으로 만들어진 신비한 목걸이를 발견한다...",
                                                                    "rating": 6.692,
                                                                    "genres": ["ACTION", "FANTASY", "ADVENTURE"]
                                                                  },
                                                                  {
                                                                    "id": 2546,
                                                                    "title": "성룡의 쿵푸마스터",
                                                                    "year": 1994,
                                                                    "posterUrl": "/poster2.jpg",
                                                                    "overview": "무술의 대가 황비홍의 이야기...",
                                                                    "rating": 7.4,
                                                                    "genres": ["ACTION", "COMEDY"]
                                                                  }
                                                                ]
                                                              },
                                                              "message": "OK"
                                                            }
                                                            """))),
                @ApiResponse(
                        responseCode = "400",
                        description = "검색 키워드 누락",
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
                                                              "message": "검색 키워드는 필수입니다"
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
    PageResponse<MovieResponse> searchMovies(String query, int page, int size);
}
