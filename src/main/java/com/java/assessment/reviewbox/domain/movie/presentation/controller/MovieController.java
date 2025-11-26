package com.java.assessment.reviewbox.domain.movie.presentation.controller;

import com.java.assessment.reviewbox.domain.movie.business.enums.MovieCategory;
import com.java.assessment.reviewbox.domain.movie.business.service.MovieService;
import com.java.assessment.reviewbox.domain.movie.presentation.controller.docs.MovieDocs;
import com.java.assessment.reviewbox.domain.movie.presentation.response.MovieDetailResponse;
import com.java.assessment.reviewbox.domain.movie.presentation.response.MovieResponse;
import com.java.assessment.reviewbox.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController implements MovieDocs {
    private final MovieService movieService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Override
    public PageResponse<MovieResponse> getMovies(
            @RequestParam(defaultValue = "popular") MovieCategory category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return movieService.getMoviesByCategory(category, page, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{movieId}")
    @Override
    public MovieDetailResponse getMovie(@PathVariable long movieId) {
        return movieService.getMovieDetail(movieId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    @Override
    public PageResponse<MovieResponse> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return movieService.searchMovies(query, page, size);
    }
}
