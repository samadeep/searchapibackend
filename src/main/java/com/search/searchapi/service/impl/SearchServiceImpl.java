package com.search.searchapi.service.impl;

import com.search.searchapi.exceptions.InvalidPaginationException;
import com.search.searchapi.exceptions.InvalidSearchRequestException;
import com.search.searchapi.exceptions.InvalidSourceException;
import com.search.searchapi.exceptions.RateLimitExceededException;
import com.search.searchapi.exceptions.SourceUnavailableException;
import com.search.searchapi.models.SearchItem;
import com.search.searchapi.models.SearchRequest;
import com.search.searchapi.models.SearchResult;
import com.search.searchapi.models.Sources;
import com.search.searchapi.ratelimit.RateLimiter;
import com.search.searchapi.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
    private final RateLimiter rateLimiter;

    @Autowired
    public SearchServiceImpl(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public SearchResult search(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        logger.info("Starting search with request: {}", request);
        
        try {
            // Validate request
            validateSearchRequest(request);

            // Get sources to search
            List<String> sources = request.getSources();
            if (sources == null || sources.isEmpty()) {
                sources = Arrays.stream(Sources.values())
                        .map(Enum::name)
                        .collect(Collectors.toList());
            }

            // Search each source
            List<SearchItem> results = new ArrayList<>();
            for (String source : sources) {
                try {
                    if (!rateLimiter.tryAcquire(source)) {
                        logger.warn("Rate limit exceeded for source: {}", source);
                        throw new RateLimitExceededException(source);
                    }
                    results.addAll(searchSource(source, request));
                } catch (Exception e) {
                    logger.error("Error searching source {}: {}", source, e.getMessage(), e);
                    if (e instanceof RateLimitExceededException || e instanceof SourceUnavailableException) {
                        throw e;
                    }
                }
            }

            // Calculate pagination
            int page = Math.max(1, request.getPage());
            int size = Math.min(100, Math.max(1, request.getSize()));
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, results.size());

            // Create response
            SearchResult searchResult = new SearchResult();
            searchResult.setItems(results.subList(fromIndex, toIndex));
            searchResult.setTotalResults(results.size());
            searchResult.setCurrentPage(page);
            searchResult.setTotalPages((int) Math.ceil((double) results.size() / size));
            searchResult.setFacets(calculateFacets(results, request.getFacets()));
            searchResult.setExecutionTime(System.currentTimeMillis() - startTime);

            logger.info("Search completed successfully in {}ms", searchResult.getExecutionTime());
            return searchResult;
        } catch (Exception e) {
            logger.error("Search failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void validateSearchRequest(SearchRequest request) {
        if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
            throw new InvalidSearchRequestException("Search query cannot be empty");
        }

        if (request.getPage() < 1) {
            throw new InvalidPaginationException("Page number must be greater than 0");
        }

        if (request.getSize() < 1 || request.getSize() > 100) {
            throw new InvalidPaginationException("Page size must be between 1 and 100");
        }

        if (request.getSources() != null) {
            for (String source : request.getSources()) {
                try {
                    Sources.valueOf(source.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new InvalidSourceException(source);
                }
            }
        }
    }

    private List<SearchItem> searchSource(String source, SearchRequest request) {
        // Simulate source unavailability
        if (Math.random() < 0.05) { // 5% chance of unavailability
            logger.warn("Source {} is currently unavailable", source);
            throw new SourceUnavailableException(source);
        }

        // Generate mock results based on the query
        List<SearchItem> results = new ArrayList<>();
        String query = request.getQuery().toLowerCase();
        
        // Create mock results based on the query
        if (query.contains("java") || query.contains("spring")) {
            results.add(createMockItem("1", "Java Spring Boot Tutorial", 
                "Learn how to build REST APIs with Spring Boot", 
                "https://example.com/java-spring-boot", source, 0.95));
            results.add(createMockItem("2", "Spring Framework Documentation", 
                "Official documentation for Spring Framework", 
                "https://example.com/spring-docs", source, 0.90));
        }
        
        if (query.contains("python") || query.contains("flask")) {
            results.add(createMockItem("3", "Python Flask API Guide", 
                "Building web APIs with Flask framework", 
                "https://example.com/python-flask", source, 0.88));
        }
        
        if (query.contains("react") || query.contains("angular")) {
            results.add(createMockItem("4", "React vs Angular Comparison", 
                "Comparison of popular frontend frameworks", 
                "https://example.com/react-angular", source, 0.92));
        }
        
        if (query.contains("docker") || query.contains("kubernetes")) {
            results.add(createMockItem("5", "Docker and Kubernetes Guide", 
                "Containerization and orchestration with Docker and Kubernetes", 
                "https://example.com/docker-k8s", source, 0.85));
        }
        
        if (query.contains("machine") || query.contains("learning")) {
            results.add(createMockItem("6", "Machine Learning Basics", 
                "Introduction to machine learning concepts", 
                "https://example.com/ml-basics", source, 0.87));
        }

        // If no specific matches, add some generic results
        if (results.isEmpty()) {
            results.add(createMockItem("7", "General Programming Guide", 
                "Comprehensive guide to programming concepts", 
                "https://example.com/programming", source, 0.80));
            results.add(createMockItem("8", "Software Development Best Practices", 
                "Best practices for software development", 
                "https://example.com/best-practices", source, 0.75));
        }

        return results;
    }

    private SearchItem createMockItem(String id, String title, String description, 
                                    String url, String source, double score) {
        SearchItem item = new SearchItem();
        item.setId(id);
        item.setTitle(title);
        item.setDescription(description);
        item.setUrl(url);
        item.setSource(source);
        item.setScore(score);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("language", getRandomLanguage());
        metadata.put("stars", (int)(Math.random() * 10000));
        metadata.put("type", getRandomType());
        item.setMetadata(metadata);
        
        return item;
    }

    private String getRandomLanguage() {
        String[] languages = {"java", "python", "javascript", "typescript", "go", "rust"};
        return languages[(int)(Math.random() * languages.length)];
    }

    private String getRandomType() {
        String[] types = {"tutorial", "documentation", "example", "guide", "reference"};
        return types[(int)(Math.random() * types.length)];
    }

    private Map<String, Map<String, Long>> calculateFacets(List<SearchItem> results, List<String> facetFields) {
        if (facetFields == null || facetFields.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Map<String, Long>> facets = new HashMap<>();
        for (String field : facetFields) {
            Map<String, Long> facetCounts = new HashMap<>();
            for (SearchItem item : results) {
                if (item.getMetadata() != null && item.getMetadata().containsKey(field)) {
                    String value = item.getMetadata().get(field).toString();
                    facetCounts.merge(value, 1L, Long::sum);
                }
            }
            facets.put(field, facetCounts);
        }
        return facets;
    }
} 