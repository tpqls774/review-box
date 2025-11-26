package com.java.assessment.reviewbox.domain.actor.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.java.assessment.reviewbox.domain.actor.presentation.mapper.ActorDetailMapper;
import com.java.assessment.reviewbox.domain.actor.presentation.mapper.ActorMapper;
import com.java.assessment.reviewbox.domain.actor.presentation.mapper.ActorMovieMapper;
import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorDetailResponse;
import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorMovieResponse;
import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorSearchResponse;
import com.java.assessment.reviewbox.global.response.PageResponse;
import com.java.assessment.reviewbox.global.tmdb.client.TmdbClient;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbActorDetailResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbActorMovieCreditsResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbPersonSearchResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActorService 단위 테스트")
class ActorServiceTest {
    @Mock
    private TmdbClient tmdbClient;

    @Mock
    private ActorMapper actorMapper;

    @Mock
    private ActorDetailMapper actorDetailMapper;

    @Mock
    private ActorMovieMapper actorMovieMapper;

    @InjectMocks
    private ActorService actorService;

    @Test
    @DisplayName("배우 검색 성공")
    void searchActors_Success() {
        String query = "Tom Hanks";
        int page = 1;
        int size = 1; // local size

        List<TmdbPersonSearchResponse.Person> tmdbPersons = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            tmdbPersons.add(mock(TmdbPersonSearchResponse.Person.class));
        }

        TmdbPersonSearchResponse tmdbResponse = mock(TmdbPersonSearchResponse.class);

        given(tmdbResponse.getTotal_results()).willReturn(100);
        given(tmdbResponse.getResults()).willReturn(tmdbPersons);
        given(tmdbResponse.getTotal_pages()).willReturn(5);

        ActorSearchResponse expectedActorResponse = mock(ActorSearchResponse.class);
        ActorSearchResponse dummyResponse = mock(ActorSearchResponse.class);

        given(actorMapper.toResponse(any())).willReturn(dummyResponse);

        given(actorMapper.toResponse(tmdbPersons.get(0))).willReturn(expectedActorResponse);

        given(tmdbClient.searchPerson(eq(query), eq(page))).willReturn(tmdbResponse);

        // when
        PageResponse<ActorSearchResponse> result = actorService.searchActors(query, page, size);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.getTotalCount()).isEqualTo(100);
        assertThat(result.isHasNext()).isTrue();

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(expectedActorResponse);

        verify(tmdbClient, times(1)).searchPerson(eq(query), eq(page));

        verify(actorMapper, times(20)).toResponse(any());
        verify(actorMapper, times(1)).toResponse(tmdbPersons.get(0));
    }

    @Test
    @DisplayName("배우 검색 성공 - 결과가 비어있는 경우")
    void searchActors_Success_EmptyResult() {
        // given
        String query = "NonExistentActor";
        int page = 1;
        int size = 10;

        TmdbPersonSearchResponse tmdbResponse = mock(TmdbPersonSearchResponse.class);

        given(tmdbResponse.getTotal_results()).willReturn(0);

        given(tmdbClient.searchPerson(anyString(), anyInt())).willReturn(tmdbResponse);

        // when
        PageResponse<ActorSearchResponse> result = actorService.searchActors(query, page, size);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalCount()).isEqualTo(0);
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.getContent()).isEmpty();

        verify(tmdbClient, times(1)).searchPerson(eq(query), anyInt());

        verify(tmdbResponse, times(1)).getTotal_results();
    }

    @Test
    @DisplayName("배우 상세 조회 성공")
    void getActor_Success() {
        // given
        long actorId = 31L; // Tom Hanks ID

        TmdbActorDetailResponse actorDetail = mock(TmdbActorDetailResponse.class);
        TmdbActorMovieCreditsResponse credits = mock(TmdbActorMovieCreditsResponse.class);
        TmdbActorMovieCreditsResponse.Movie castMovie = mock(TmdbActorMovieCreditsResponse.Movie.class);

        given(tmdbClient.getActorDetail(actorId)).willReturn(actorDetail);
        given(tmdbClient.getActorMovieCredits(actorId)).willReturn(credits);
        given(credits.getCast()).willReturn(List.of(castMovie));

        ActorMovieResponse movieResponse = mock(ActorMovieResponse.class);
        given(actorMovieMapper.toResponse(any())).willReturn(movieResponse);

        ActorDetailResponse expectedResponse = mock(ActorDetailResponse.class);
        given(actorDetailMapper.toResponse(eq(actorDetail), anyList())).willReturn(expectedResponse);

        // when
        ActorDetailResponse result = actorService.getActor(actorId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResponse);
        verify(tmdbClient, times(1)).getActorDetail(actorId);
        verify(tmdbClient, times(1)).getActorMovieCredits(actorId);
        verify(actorMovieMapper, times(1)).toResponse(castMovie);
        verify(actorDetailMapper, times(1)).toResponse(eq(actorDetail), anyList());
    }

    @Test
    @DisplayName("배우 상세 조회 성공 - 출연 영화가 없는 경우")
    void getActor_Success_NoMovies() {
        // given
        long actorId = 1L;

        TmdbActorDetailResponse actorDetail = mock(TmdbActorDetailResponse.class);
        TmdbActorMovieCreditsResponse credits = mock(TmdbActorMovieCreditsResponse.class);

        given(tmdbClient.getActorDetail(actorId)).willReturn(actorDetail);
        given(tmdbClient.getActorMovieCredits(actorId)).willReturn(credits);
        given(credits.getCast()).willReturn(List.of());

        ActorDetailResponse expectedResponse = mock(ActorDetailResponse.class);
        given(actorDetailMapper.toResponse(eq(actorDetail), anyList())).willReturn(expectedResponse);

        // when
        ActorDetailResponse result = actorService.getActor(actorId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResponse);
        verify(tmdbClient, times(1)).getActorDetail(actorId);
        verify(tmdbClient, times(1)).getActorMovieCredits(actorId);
        verify(actorMovieMapper, never()).toResponse(any());
        verify(actorDetailMapper, times(1)).toResponse(eq(actorDetail), eq(List.of()));
    }

    @Test
    @DisplayName("배우 검색 실패 - TMDB API 호출 실패")
    void searchActors_Fail_TmdbApiError() {
        // given
        String query = "Tom Hanks";
        int page = 1;
        int size = 20;

        given(tmdbClient.searchPerson(anyString(), anyInt())).willThrow(new RuntimeException("TMDB API Error"));

        // when & then
        assertThatThrownBy(() -> actorService.searchActors(query, page, size))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("TMDB API Error");

        verify(tmdbClient, times(1)).searchPerson(eq(query), anyInt());
        verify(actorMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("배우 상세 조회 실패 - getActorDetail API 호출 실패")
    void getActor_Fail_GetActorDetailError() {
        // given
        long actorId = 31L;

        given(tmdbClient.getActorDetail(actorId)).willThrow(new RuntimeException("Failed to fetch actor details"));

        // when & then
        assertThatThrownBy(() -> actorService.getActor(actorId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to fetch actor details");

        verify(tmdbClient, times(1)).getActorDetail(actorId);
        verify(tmdbClient, never()).getActorMovieCredits(anyLong());
        verify(actorDetailMapper, never()).toResponse(any(), anyList());
    }

    @Test
    @DisplayName("배우 상세 조회 실패 - getActorMovieCredits API 호출 실패")
    void getActor_Fail_GetActorMovieCreditsError() {
        // given
        long actorId = 31L;
        TmdbActorDetailResponse actorDetail = mock(TmdbActorDetailResponse.class);

        given(tmdbClient.getActorDetail(actorId)).willReturn(actorDetail);
        given(tmdbClient.getActorMovieCredits(actorId))
                .willThrow(new RuntimeException("Failed to fetch movie credits"));

        // when & then
        assertThatThrownBy(() -> actorService.getActor(actorId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to fetch movie credits");

        verify(tmdbClient, times(1)).getActorDetail(actorId);
        verify(tmdbClient, times(1)).getActorMovieCredits(actorId);
        verify(actorDetailMapper, never()).toResponse(any(), anyList());
    }

    @Test
    @DisplayName("배우 검색 실패 - null 응답 처리")
    void searchActors_Fail_NullResponse() {
        // given
        String query = "Tom Hanks";
        int page = 1;
        int size = 20;

        given(tmdbClient.searchPerson(anyString(), anyInt())).willReturn(null);

        // when & then
        assertThatThrownBy(() -> actorService.searchActors(query, page, size)).isInstanceOf(NullPointerException.class);

        verify(tmdbClient, times(1)).searchPerson(eq(query), anyInt());
    }

    @Test
    @DisplayName("배우 상세 조회 실패 - null 응답 처리")
    void getActor_Fail_NullActorDetailResponse() {
        // given
        long actorId = 31L;

        given(tmdbClient.getActorDetail(actorId)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> actorService.getActor(actorId)).isInstanceOf(NullPointerException.class);

        verify(tmdbClient, times(1)).getActorDetail(actorId);
    }
}
