package com.java.assessment.reviewbox.domain.actor.business.service;

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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final TmdbClient tmdbClient;
    private final ActorMapper actorMapper;
    private final ActorDetailMapper actorDetailMapper;
    private final ActorMovieMapper actorMovieMapper;

    public PageResponse<ActorSearchResponse> searchActors(String query, int page, int size) {
        int tmdbPageSize = 20;

        int startIndex = (page - 1) * size;
        int startTmdbPage = startIndex / tmdbPageSize + 1;

        int localOffsetInFirstTmdbPage = startIndex % tmdbPageSize;

        int usableFromFirstPage = tmdbPageSize - localOffsetInFirstTmdbPage;
        int remainingNeeded = size - usableFromFirstPage;

        int numTmdbPagesToFetch = 1;

        if (remainingNeeded > 0) {
            numTmdbPagesToFetch += (remainingNeeded + tmdbPageSize - 1) / tmdbPageSize;
        }

        int endTmdbPage = startTmdbPage + numTmdbPagesToFetch - 1;

        List<ActorSearchResponse> allFetched = new ArrayList<>();
        long totalCount = 0;

        for (int p = startTmdbPage; p <= endTmdbPage; p++) {
            TmdbPersonSearchResponse response = tmdbClient.searchPerson(query, p);

            if (p == startTmdbPage) {
                totalCount = response.getTotal_results();
                if (p > response.getTotal_pages()) {
                    break;
                }
            }

            List<ActorSearchResponse> mapped =
                    response.getResults().stream().map(actorMapper::toResponse).toList();

            allFetched.addAll(mapped);
        }

        List<ActorSearchResponse> sliced =
                allFetched.stream().skip(localOffsetInFirstTmdbPage).limit(size).collect(Collectors.toList());

        int endIndex = startIndex + sliced.size();
        boolean hasNext = endIndex < totalCount;

        return PageResponse.of(page, size, totalCount, hasNext, sliced);
    }

    public ActorDetailResponse getActor(long actorId) {
        TmdbActorDetailResponse actor = tmdbClient.getActorDetail(actorId);
        TmdbActorMovieCreditsResponse credits = tmdbClient.getActorMovieCredits(actorId);

        List<ActorMovieResponse> movies =
                credits.getCast().stream().map(actorMovieMapper::toResponse).toList();

        return actorDetailMapper.toResponse(actor, movies);
    }
}
