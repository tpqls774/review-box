package com.java.assessment.reviewbox.domain.movie.presentation.converter;

import com.java.assessment.reviewbox.domain.movie.business.enums.MovieCategory;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieCategoryConverter implements Converter<String, MovieCategory> {
    @Override
    public MovieCategory convert(@NonNull String source) {
        return MovieCategory.valueOf(source.toUpperCase());
    }
}
