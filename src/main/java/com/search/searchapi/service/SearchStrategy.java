package com.search.searchapi.service;

import com.search.searchapi.models.SearchItem;
import com.search.searchapi.models.SearchRequest;

import java.util.List;

public interface SearchStrategy {
    List<SearchItem> search(String source, SearchRequest request);
    boolean supports(String source);
    double getRelevanceScore(String query, SearchItem item);
} 