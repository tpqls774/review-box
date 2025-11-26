package com.java.assessment.reviewbox.domain.actor.presentation.controller;

import com.java.assessment.reviewbox.domain.actor.business.service.ActorService;
import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorDetailResponse;
import com.java.assessment.reviewbox.domain.actor.presentation.response.ActorSearchResponse;
import com.java.assessment.reviewbox.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @GetMapping("/search")
    public PageResponse<ActorSearchResponse> searchActors(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return actorService.searchActors(query, page, size);
    }

    @GetMapping("/{actorId}")
    public ActorDetailResponse getActor(@PathVariable long actorId) {
        return actorService.getActor(actorId);
    }
}
