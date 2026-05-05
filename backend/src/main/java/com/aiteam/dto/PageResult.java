package com.aiteam.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageResult<T> {
    private List<T> data;
    private Long total;
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;

    public PageResult(List<T> data, Long total, Integer page, Integer size) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) total / size);
        this.hasNext = page < totalPages;
        this.hasPrevious = page > 1;
    }

    public static <T> PageResult<T> of(List<T> data, Long total, Integer page, Integer size) {
        return new PageResult<>(data, total, page, size);
    }
}