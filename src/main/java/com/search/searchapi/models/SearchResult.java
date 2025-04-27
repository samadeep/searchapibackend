package com.search.searchapi.models;

import java.util.List;
import java.util.Map;

public class SearchResult {
    private List<SearchItem> items;
    private long totalResults;
    private int currentPage;
    private int totalPages;
    private Map<String, Map<String, Long>> facets;
    private long executionTime;

    // Getters and Setters
    public List<SearchItem> getItems() {
        return items;
    }

    public void setItems(List<SearchItem> items) {
        this.items = items;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Map<String, Map<String, Long>> getFacets() {
        return facets;
    }

    public void setFacets(Map<String, Map<String, Long>> facets) {
        this.facets = facets;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
} 