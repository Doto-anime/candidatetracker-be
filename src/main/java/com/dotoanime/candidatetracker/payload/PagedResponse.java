package com.dotoanime.candidatetracker.payload;

import lombok.Data;

import java.util.List;

@Data
public class PagedResponse<T> {

    private Boolean success = true;
    private String message = "List of user's job retrieved";
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PagedResponse() {

    }

    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }
}
