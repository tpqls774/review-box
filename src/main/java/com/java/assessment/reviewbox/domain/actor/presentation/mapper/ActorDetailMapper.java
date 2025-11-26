package com.java.assessment.reviewbox.domain.actor.presentation.mapper;

import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorDetailResponse;
import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorMovieResponse;
import com.java.assessment.reviewbox.global.tmdb.constants.TmdbConstants;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbActorDetailResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ActorDetailMapper {
    @Mapping(target = "profileUrl", source = "actor.profilePath", qualifiedByName = "poster")
    @Mapping(target = "birthday", source = "actor.birthday")
    @Mapping(target = "birthplace", source = "actor.placeOfBirth")
    @Mapping(target = "biography", source = "actor.biography")
    @Mapping(target = "movies", source = "movies")
    ActorDetailResponse toResponse(TmdbActorDetailResponse actor, List<ActorMovieResponse> movies);

    @Named("poster")
    default String mapPoster(String path) {
        if (path == null) return null;

        return TmdbConstants.TMDB_IMAGE_BASE_URL + path;
    }
}
