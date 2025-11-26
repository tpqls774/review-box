package com.java.assessment.reviewbox.domain.actor.presentation.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.java.assessment.reviewbox.domain.actor.business.exception.ActorNotFoundException;
import com.java.assessment.reviewbox.domain.actor.business.service.ActorService;
import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorDetailResponse;
import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorSearchResponse;
import com.java.assessment.reviewbox.global.response.PageResponse;
import com.java.assessment.reviewbox.global.security.jwt.extractor.JwtExtractor;
import com.java.assessment.reviewbox.global.security.jwt.provider.JwtProvider;
import com.java.assessment.reviewbox.global.security.jwt.validator.JwtValidator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = ActorController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@DisplayName("ActorController 단위 테스트")
class ActorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActorService actorService;

    @MockitoBean
    private JwtExtractor jwtExtractor;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private JwtValidator jwtValidator;

    @Test
    @DisplayName("GET /actors/search?query=Tom - 배우 검색 성공")
    void searchActors_Success() throws Exception {
        // given
        ActorSearchResponse actorResponse = mock(ActorSearchResponse.class);
        PageResponse<ActorSearchResponse> pageResponse = PageResponse.of(1, 1, 50, true, List.of(actorResponse));

        given(actorService.searchActors("Tom", 1, 20)).willReturn(pageResponse);

        // when & then
        mockMvc.perform(get("/actors/search").param("query", "Tom").param("page", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.size").value(1))
                .andExpect(jsonPath("$.data.totalCount").value(50))
                .andExpect(jsonPath("$.data.hasNext").value(true));
    }

    @Test
    @DisplayName("GET /actors/search?query= - 빈 쿼리로 검색")
    void searchActors_EmptyQuery() throws Exception {
        // given
        PageResponse<ActorSearchResponse> emptyResponse = PageResponse.of(1, 0, 0, false, List.of());

        given(actorService.searchActors(anyString(), anyInt(), anyInt())).willReturn(emptyResponse);

        // when & then
        mockMvc.perform(get("/actors/search").param("query", "").param("page", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size").value(0))
                .andExpect(jsonPath("$.data.content").isEmpty());
    }

    @Test
    @DisplayName("GET /actors/{actorId} - 배우 상세 조회 성공")
    void getActor_Success() throws Exception {
        // given
        ActorDetailResponse actorDetail = mock(ActorDetailResponse.class);
        given(actorService.getActor(31L)).willReturn(actorDetail);

        // when & then
        mockMvc.perform(get("/actors/{actorId}", 31L)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /actors/{actorId} - 배우 상세 조회 실패 (존재하지 않는 배우)")
    void getActor_Fail_NotFound() throws Exception {
        // given
        long actorId = 9999L;
        given(actorService.getActor(actorId)).willThrow(ActorNotFoundException.EXCEPTION);

        // when & then
        mockMvc.perform(get("/actors/{actorId}", actorId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ACTOR_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("배우를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("GET /actors/search - 기본 페이지 파라미터 사용")
    void searchActors_DefaultPageParameter() throws Exception {
        // given
        ActorSearchResponse actorResponse = mock(ActorSearchResponse.class);
        PageResponse<ActorSearchResponse> pageResponse = PageResponse.of(1, 1, 10, false, List.of(actorResponse));

        given(actorService.searchActors("Actor", 1, 20)).willReturn(pageResponse);

        // when & then
        mockMvc.perform(get("/actors/search").param("query", "Actor"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.page").value(1));
    }
}
