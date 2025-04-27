package com.search.searchapi.service.impl;

import com.search.searchapi.models.SearchItem;
import com.search.searchapi.models.SearchRequest;
import com.search.searchapi.service.SearchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractSearchStrategy implements SearchStrategy {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<SearchItem> search(String source, SearchRequest request) {
        List<SearchItem> results = doSearch(source, request);
        return results.stream()
                .map(item -> {
                    item.setScore(getRelevanceScore(request.getQuery(), item));
                    return item;
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());
    }

    protected abstract List<SearchItem> doSearch(String source, SearchRequest request);

    @Override
    public double getRelevanceScore(String query, SearchItem item) {
        if (query == null || item == null) return 0.0;

        String normalizedQuery = query.toLowerCase();
        String normalizedTitle = item.getTitle().toLowerCase();
        String normalizedDescription = item.getDescription() != null ? 
                                     item.getDescription().toLowerCase() : "";

        double score = 0.0;

        // Title match has higher weight
        if (normalizedTitle.contains(normalizedQuery)) {
            score += 0.6;
        }

        // Description match has lower weight
        if (normalizedDescription.contains(normalizedQuery)) {
            score += 0.4;
        }

        // Exact match bonus
        if (normalizedTitle.equals(normalizedQuery)) {
            score += 0.2;
        }

        // Partial word match
        String[] queryWords = normalizedQuery.split("\\s+");
        for (String word : queryWords) {
            if (normalizedTitle.contains(word)) {
                score += 0.1;
            }
            if (normalizedDescription.contains(word)) {
                score += 0.05;
            }
        }

        return Math.min(1.0, score);
    }

    protected SearchItem createSearchItem(String id, String title, String description, 
                                       String url, String source, Map<String, Object> metadata) {
        SearchItem item = new SearchItem();
        item.setId(id);
        item.setTitle(title);
        item.setDescription(description);
        item.setUrl(url);
        item.setSource(source);
        item.setMetadata(metadata);
        return item;
    }
} 