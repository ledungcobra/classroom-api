package com.ledungcobra.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

public class PageableBuilder {

    private PageableBuilder(){

    }

    public static Pageable getPageable(Integer startAt, Integer maxResults, String sortColumn) {
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;

        Sort sort = null;
        var newSortColumn = sortColumn == null ? null : sortColumn.charAt(0) + (sortColumn.charAt(1) + "").
                toLowerCase() + sortColumn.substring(2);

        if (newSortColumn != null) {
            if (newSortColumn.contains("-")) {
                sort = JpaSort.unsafe(newSortColumn.replace("-", "").replace(" ","")).descending();
            } else {
                sort = JpaSort.unsafe(newSortColumn.replace("+", "").replace(" ","")).ascending();
            }
        } else {
            sort = Sort.by("id").ascending();
        }

        return new OffsetPageable(startAt, maxResults, sort);
    }
}
