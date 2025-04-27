package com.search.searchapi.service;

import com.search.searchapi.models.SearchRequest;
import com.search.searchapi.models.SearchResult;

public interface SearchService {
    SearchResult search(SearchRequest request);
} 