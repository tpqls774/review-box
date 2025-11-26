package com.java.assessment.reviewbox.domain.movie.persistence.entity;

import com.java.assessment.reviewbox.domain.movie.persistence.enums.MovieGenre;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class MovieEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    private int year;

    @Column(columnDefinition = "TEXT", name = "poster_url")
    private String posterUrl;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false)
    private String director;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre")
    private Set<MovieGenre> genres = new HashSet<>();

    private double rating;
    private int runtime;
}
