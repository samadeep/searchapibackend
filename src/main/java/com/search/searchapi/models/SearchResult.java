package com.search.searchapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

@Schema(description = "Search result response")
public class SearchResult {
    @Schema(description = "List of search results")
    private List<SearchItem> items;

    @Schema(description = "Total number of results found")
    private long totalResults;

    @Schema(description = "Current page number")
    private int currentPage;

    @Schema(description = "Total number of pages")
    private int totalPages;

    @Schema(description = "Facet counts for requested fields")
    private Map<String, Map<String, Long>> facets;

    @Schema(description = "Search execution time in milliseconds")
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