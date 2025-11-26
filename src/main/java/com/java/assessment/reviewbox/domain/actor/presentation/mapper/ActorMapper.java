package com.java.assessment.reviewbox.domain.actor.presentation.mapper;

import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorSearchResponse;
import com.java.assessment.reviewbox.global.tmdb.constants.TmdbConstants;
import com.java.assessment.reviewbox.global.tmdb.response.TmdbPersonSearchResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ActorMapper {
    @Mapping(target = "profileUrl", source = "person.profilePath", qualifiedByName = "poster")
    @Mapping(target = "knownForDepartment", source = "person.knownForDepartment")
    @Mapping(target = "knownFor", source = "person.knownFor", qualifiedByName = "knownFor")
    ActorSearchResponse toResponse(TmdbPersonSearchResponse.Person person);

    @Named("poster")
    default String mapPoster(String path) {
        if (path == null) return null;
        return TmdbConstants.TMDB_IMAGE_BASE_URL + path;
    }

    @Named("knownFor")
    default List<ActorSearchResponse.KnownFor> mapKnownFor(List<TmdbPersonSearchResponse.Person.KnownFor> list) {
        if (list == null) return List.of();

        return list.stream()
                .map(item -> ActorSearchResponse.KnownFor.of(
                        item.getId(), item.getTitle(), mapPoster(item.getPosterPath()), item.getMediaType()))
                .collect(Collectors.toList());
    }
}
