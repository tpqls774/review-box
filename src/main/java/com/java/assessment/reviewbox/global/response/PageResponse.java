package com.java.assessment.reviewbox.global.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
@Getter
public class PageResponse<T> {
    private final int page;
    private final int size;
    private final long totalCount;
    private final boolean hasNext;
    private final List<T> content;

    public static <T> PageResponse<T> of(int page, int size, long totalCount, boolean hasNext, List<T> content) {
        return new PageResponse<>(page, size, totalCount, hasNext, content);
    }

    public static <T> PageResponse<T> from(Page<T> page) {
        return of(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.hasNext(), page.getContent());
    }
}
