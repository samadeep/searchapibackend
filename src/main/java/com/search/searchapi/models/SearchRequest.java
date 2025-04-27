package com.search.searchapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;

@Schema(description = "Search request parameters")
public class SearchRequest {
    @NotBlank(message = "Search query cannot be blank")
    @Size(min = 2, max = 500, message = "Query must be between 2 and 500 characters")
    @Schema(description = "Search query string", example = "spring boot tutorial")
    private String query;

    @Min(value = 1, message = "Page number must be greater than 0")
    @Schema(description = "Page number (1-based)", example = "1", defaultValue = "1")
    private int page = 1;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size must not exceed 100")
    @Schema(description = "Number of results per page", example = "10", defaultValue = "10")
    private int size = 10;

    @Schema(description = "List of sources to search from", example = "[\"GITHUB\", \"STACKOVERFLOW\"]")
    private List<String> sources;

    @Schema(description = "Map of filters to apply to the search results")
    private Map<String, List<String>> filters;

    @Schema(description = "List of fields to generate facets for", example = "[\"language\", \"type\"]")
    private List<String> facets;

    @Pattern(regexp = "^(relevance|date|stars)$", message = "Sort by must be one of: relevance, date, stars")
    @Schema(description = "Field to sort results by", example = "relevance", defaultValue = "relevance")
    private String sortBy = "relevance";

    @Pattern(regexp = "^(asc|desc)$", message = "Sort order must be either asc or desc")
    @Schema(description = "Sort order (asc or desc)", example = "desc", defaultValue = "desc")
    private String sortOrder = "desc";

    // Getters and Setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<String>> filters) {
        this.filters = filters;
    }

    public List<String> getFacets() {
        return facets;
    }

    public void setFacets(List<String> facets) {
        this.facets = facets;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
} 