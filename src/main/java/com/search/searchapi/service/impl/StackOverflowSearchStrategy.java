package com.search.searchapi.service.impl;

import com.search.searchapi.models.SearchItem;
import com.search.searchapi.models.SearchRequest;
import com.search.searchapi.models.Sources;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StackOverflowSearchStrategy extends AbstractSearchStrategy {

    @Override
    public boolean supports(String source) {
        return Sources.STACKOVERFLOW.name().equals(source);
    }

    @Override
    protected List<SearchItem> doSearch(String source, SearchRequest request) {
        List<SearchItem> results = new ArrayList<>();
        String query = request.getQuery().toLowerCase();

        // Simulate StackOverflow search results
        if (query.contains("spring") || query.contains("java")) {
            results.add(createSearchItem(
                "so_1",
                "How to create a REST API with Spring Boot?",
                "Step-by-step guide to creating a REST API using Spring Boot",
                "https://stackoverflow.com/questions/spring-boot-rest-api",
                source,
                createMetadata("java", 1000, "question")
            ));

            results.add(createSearchItem(
                "so_2",
                "Spring Boot vs Spring Framework",
                "Comparison between Spring Boot and Spring Framework",
                "https://stackoverflow.com/questions/spring-boot-vs-framework",
                source,
                createMetadata("java", 800, "question")
            ));
        }

        if (query.contains("python") || query.contains("flask")) {
            results.add(createSearchItem(
                "so_3",
                "Flask vs Django for REST APIs",
                "Comparison of Flask and Django for building REST APIs",
                "https://stackoverflow.com/questions/flask-vs-django",
                source,
                createMetadata("python", 1200, "question")
            ));
        }

        return results;
    }

    private Map<String, Object> createMetadata(String language, int votes, String type) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("language", language);
        metadata.put("votes", votes);
        metadata.put("type", type);
        return metadata;
    }
} 