package com.ledungcobra.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableBuilder {
    public static Pageable getPageable(Integer startAt, Integer maxResults, String sortColumn) {
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;

        Sort sort = null;

        if (sortColumn != null) {
            if (sortColumn.contains("-")) {
                sort = Sort.by(sortColumn.replace("-", "")).descending();
            } else {
                sort = Sort.by(sortColumn.replace("+", "")).ascending();
            }
        } else {
            sort = Sort.by("id").ascending();
        }

        return new OffsetPageable(startAt, maxResults, sort);
    }
}
