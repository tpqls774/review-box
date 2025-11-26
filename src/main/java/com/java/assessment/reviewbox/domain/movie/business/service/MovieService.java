package com.java.assessment.reviewbox.domain.movie.business.service;

import com.java.assessment.reviewbox.domain.movie.business.enums.MovieCategory;
import com.java.assessment.reviewbox.domain.movie.persistence.entity.MovieEntity;
import com.java.assessment.reviewbox.domain.movie.persistence.enums.MovieGenre;
import com.java.assessment.reviewbox.domain.movie.persistence.repository.MovieRepository;
import com.java.assessment.reviewbox.domain.movie.presentation.mapper.MovieDetailMapper;
import com.java.assessment.reviewbox.domain.movie.presentation.mapper.MovieMapper;
import com.java.assessment.reviewbox.domain.movie.presentation.response.MovieDetailResponse;
import com.java.assessment.reviewbox.domain.movie.presentation.response.MovieResponse;
import com.java.assessment.reviewbox.domain.review.persistence.repository.ReviewRepository;
import com.java.assessment.reviewbox.global.response.PageResponse;
import com.java.assessment.reviewbox.global.tmdb.client.TmdbClient;
import com.java.assessment.reviewbox.global.tmdb.constants.TmdbConstants;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbCreditResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbMovieDetailResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbMovieListResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final TmdbClient tmdbClient;
    private final MovieMapper movieMapper;
    private final MovieDetailMapper movieDetailMapper;

    public PageResponse<MovieResponse> getMoviesByCategory(MovieCategory category, int page, int size) {
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

        List<MovieResponse> allFetched = new ArrayList<>();
        long totalCount = 0;

        for (int p = startTmdbPage; p <= endTmdbPage; p++) {
            TmdbMovieListResponse response =
                    switch (category) {
                        case POPULAR -> tmdbClient.getPopularMovies(p);
                        case NOW_PLAYING -> tmdbClient.getPlayingMovies(p);
                        case UPCOMING -> tmdbClient.getUpcomingMovies(p);
                        case TOP_RATED -> tmdbClient.getTopRatedMovies(p);
                    };

            if (p == startTmdbPage) {
                totalCount = response.getTotalResults();
                if (p > response.getTotalPages()) {
                    break;
                }
            }

            List<MovieResponse> mapped =
                    response.getResults().stream().map(movieMapper::toResponse).toList();

            allFetched.addAll(mapped);
        }

        List<MovieResponse> sliced =
                allFetched.stream().skip(localOffsetInFirstTmdbPage).limit(size).collect(Collectors.toList());

        int endIndex = startIndex + sliced.size();
        boolean hasNext = endIndex < totalCount;

        return PageResponse.of(page, size, totalCount, hasNext, sliced);
    }

    @Transactional
    public MovieDetailResponse getMovieDetail(long movieId) {
        Optional<MovieEntity> optionalMovie = movieRepository.findById(movieId);

        long reviewCount = reviewRepository.countByMovieId(movieId);

        if (optionalMovie.isPresent()) {
            MovieEntity movie = optionalMovie.get();
            TmdbCreditResponse credits = tmdbClient.getMovieCredits(movieId);
            String director = extractDirector(credits);
            List<MovieDetailResponse.Cast> cast = extractCast(credits);

            return MovieDetailResponse.of(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getYear(),
                    movie.getPosterUrl(),
                    movie.getSummary(),
                    movie.getRating(),
                    movie.getRuntime(),
                    movie.getGenres().stream().map(Enum::name).collect(Collectors.toSet()),
                    director,
                    cast,
                    reviewCount);
        }

        return fetchAndSave(movieId);
    }

    public PageResponse<MovieResponse> searchMovies(String query, int page, int size) {
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

        List<MovieResponse> allFetched = new ArrayList<>();
        long totalCount = 0;

        for (int p = startTmdbPage; p <= endTmdbPage; p++) {
            TmdbMovieListResponse response = tmdbClient.searchMovies(query, p);

            if (p == startTmdbPage) {
                totalCount = response.getTotalResults();
                if (p > response.getTotalPages()) {
                    break;
                }
            }

            List<MovieResponse> mapped =
                    response.getResults().stream().map(movieMapper::toResponse).toList();

            allFetched.addAll(mapped);
        }

        List<MovieResponse> sliced =
                allFetched.stream().skip(localOffsetInFirstTmdbPage).limit(size).collect(Collectors.toList());

        int endIndex = startIndex + sliced.size();
        boolean hasNext = endIndex < totalCount;

        return PageResponse.of(page, size, totalCount, hasNext, sliced);
    }

    private MovieDetailResponse fetchAndSave(long movieId) {
        TmdbMovieDetailResponse detail = tmdbClient.getMovieDetail(movieId);
        TmdbCreditResponse credits = tmdbClient.getMovieCredits(movieId);
        String director = extractDirector(credits);
        MovieEntity entity = convertTmdbToEntity(detail, director);

        movieRepository.save(entity);

        long reviewCount = reviewRepository.countByMovieId(movieId);
        MovieDetailResponse base = movieDetailMapper.toResponse(detail);

        return base.toBuilder()
                .director(director)
                .cast(extractCast(credits))
                .reviewCount(reviewCount)
                .build();
    }

    private String extractDirector(TmdbCreditResponse credits) {
        return credits.getCrew().stream()
                .filter(c -> "Director".equalsIgnoreCase(c.getJob()))
                .map(TmdbCreditResponse.Crew::getName)
                .findFirst()
                .orElse("Unknown");
    }

    private List<MovieDetailResponse.Cast> extractCast(TmdbCreditResponse credits) {
        return credits.getCast().stream()
                .limit(10)
                .map(c -> new MovieDetailResponse.Cast(
                        c.getId(), c.getName(), c.getCharacter(), mapProfilePath(c.getProfilePath())))
                .toList();
    }

    private String mapProfilePath(String profilePath) {
        if (profilePath == null) return null;

        return TmdbConstants.TMDB_IMAGE_BASE_URL + profilePath;
    }

    private MovieEntity convertTmdbToEntity(TmdbMovieDetailResponse detail, String director) {
        Set<MovieGenre> genres = detail.getGenres().stream()
                .map(g -> MovieGenre.fromTmdbId(g.getId()))
                .collect(Collectors.toSet());

        return MovieEntity.builder()
                .id(detail.getId())
                .title(detail.getTitle())
                .year(Integer.parseInt(detail.getReleaseDate().substring(0, 4)))
                .posterUrl(TmdbConstants.TMDB_IMAGE_BASE_URL + detail.getPosterPath())
                .summary(detail.getOverview())
                .rating(detail.getVoteAverage())
                .runtime(detail.getRuntime())
                .genres(genres)
                .director(director)
                .build();
    }
}
