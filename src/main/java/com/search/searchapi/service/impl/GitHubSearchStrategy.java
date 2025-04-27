package com.search.searchapi.service.impl;

import com.search.searchapi.models.SearchItem;
import com.search.searchapi.models.SearchRequest;
import com.search.searchapi.models.Sources;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GitHubSearchStrategy extends AbstractSearchStrategy {

    @Override
    public boolean supports(String source) {
        return Sources.GITHUB.name().equals(source);
    }

    @Override
    protected List<SearchItem> doSearch(String source, SearchRequest request) {
        List<SearchItem> results = new ArrayList<>();
        String query = request.getQuery().toLowerCase();

        // Simulate GitHub search results
        if (query.contains("spring") || query.contains("java")) {
            results.add(createSearchItem(
                "github_1",
                "Spring Boot Starter",
                "Official Spring Boot starter project",
                "https://github.com/spring-projects/spring-boot-starter",
                source,
                createMetadata("java", 50000, "repository")
            ));

            results.add(createSearchItem(
                "github_2",
                "Spring Framework",
                "Spring Framework core project",
                "https://github.com/spring-projects/spring-framework",
                source,
                createMetadata("java", 45000, "repository")
            ));
        }

        if (query.contains("python") || query.contains("flask")) {
            results.add(createSearchItem(
                "github_3",
                "Flask Web Framework",
                "Python web framework",
                "https://github.com/pallets/flask",
                source,
                createMetadata("python", 60000, "repository")
            ));
        }

        return results;
    }

    private Map<String, Object> createMetadata(String language, int stars, String type) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("language", language);
        metadata.put("stars", stars);
        metadata.put("type", type);
        return metadata;
    }
} 