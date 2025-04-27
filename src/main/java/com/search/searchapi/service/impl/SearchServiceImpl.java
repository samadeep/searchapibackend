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
import com.search.searchapi.service.SearchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
    private final RateLimiter rateLimiter;
    private final List<SearchStrategy> searchStrategies;

    @Autowired
    public SearchServiceImpl(RateLimiter rateLimiter, List<SearchStrategy> searchStrategies) {
        this.rateLimiter = rateLimiter;
        this.searchStrategies = searchStrategies;
    }

    @Override
    @Cacheable(value = "searchResults", key = "#request.query + '-' + #request.page + '-' + #request.size + '-' + #request.sources?.toString()", unless = "#result == null")
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

                    SearchStrategy strategy = findStrategy(source);
                    if (strategy != null) {
                        List<SearchItem> sourceResults = strategy.search(source, request);
                        if (sourceResults != null && !sourceResults.isEmpty()) {
                            results.addAll(sourceResults);
                        }
                    } else {
                        logger.warn("No search strategy found for source: {}", source);
                    }
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

            logger.info("Search completed successfully in {}ms with {} results", 
                       searchResult.getExecutionTime(), results.size());
            return searchResult;
        } catch (Exception e) {
            logger.error("Search failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    private SearchStrategy findStrategy(String source) {
        return searchStrategies.stream()
                .filter(strategy -> strategy.supports(source))
                .findFirst()
                .orElse(null);
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