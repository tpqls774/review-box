package com.java.assessment.reviewbox.domain.movie.persistence.enums;

import com.java.assessment.reviewbox.domain.movie.business.exception.GenreNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MovieGenre {
    ACTION(28),
    ADVENTURE(12),
    ANIMATION(16),
    COMEDY(35),
    CRIME(80),
    DOCUMENTARY(99),
    DRAMA(18),
    FAMILY(10751),
    FANTASY(14),
    HISTORY(36),
    HORROR(27),
    MUSIC(10402),
    MYSTERY(9648),
    ROMANCE(10749),
    SCIENCE_FICTION(878),
    TV_MOVIE(10770),
    THRILLER(53),
    WAR(10752),
    WESTERN(37);

    private final int tmdbId;

    private static final Map<Integer, MovieGenre> GENRE_MAP =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(MovieGenre::getTmdbId, g -> g));

    public static MovieGenre fromTmdbId(int tmdbId) {
        MovieGenre genre = GENRE_MAP.get(tmdbId);

        if (genre == null) {
            throw GenreNotFoundException.EXCEPTION;
        }

        return genre;
    }
}
