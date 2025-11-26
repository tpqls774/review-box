package com.java.assessment.reviewbox.global.tmdb.client;

import com.java.assessment.reviewbox.global.tmdb.config.TmdbClientConfig;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbActorDetailResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbActorMovieCreditsResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbCreditResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbMovieDetailResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbMovieListResponse;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbPersonSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tmdbClient", url = "https://api.themoviedb.org/3", configuration = TmdbClientConfig.class)
public interface TmdbClient {
    @GetMapping("/movie/popular")
    TmdbMovieListResponse getPopularMovies(@RequestParam(value = "page", defaultValue = "1") int page);

    @GetMapping("/movie/now_playing")
    TmdbMovieListResponse getPlayingMovies(@RequestParam(value = "page", defaultValue = "1") int page);

    @GetMapping("/movie/upcoming")
    TmdbMovieListResponse getUpcomingMovies(@RequestParam(value = "page", defaultValue = "1") int page);

    @GetMapping("/movie/top_rated")
    TmdbMovieListResponse getTopRatedMovies(@RequestParam(value = "page", defaultValue = "1") int page);

    @GetMapping("/search/movie")
    TmdbMovieListResponse searchMovies(
            @RequestParam("query") String query, @RequestParam(value = "page", defaultValue = "1") int page);

    @GetMapping("/movie/{movieId}")
    TmdbMovieDetailResponse getMovieDetail(@PathVariable("movieId") long movieId);

    @GetMapping("/movie/{movieId}/credits")
    TmdbCreditResponse getMovieCredits(@PathVariable("movieId") long movieId);

    @GetMapping("/search/person")
    TmdbPersonSearchResponse searchPerson(
            @RequestParam("query") String query, @RequestParam(value = "page", defaultValue = "1") int page);

    @GetMapping("/person/{personId}")
    TmdbActorDetailResponse getActorDetail(@PathVariable("personId") long personId);

    @GetMapping("/person/{personId}/movie_credits")
    TmdbActorMovieCreditsResponse getActorMovieCredits(@PathVariable("personId") long personId);
}
