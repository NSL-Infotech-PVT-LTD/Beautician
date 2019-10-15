package com.wellgel.london.Customer;

import java.util.ArrayList;
import java.util.List;

public class PagedList<T> {

    private int page = 0;
    private List<T> results = new ArrayList<>();
    private int totalResults = 0;
    private int totalPages = 0;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}