package com.search.searchapi.controller;

import com.search.searchapi.models.SearchRequest;
import com.search.searchapi.models.SearchResult;
import com.search.searchapi.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request) {
        try {
            SearchResult result = searchService.search(request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<SearchResult> search(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) List<String> sources,
            @RequestParam(required = false) List<String> facets) {
        
        SearchRequest request = new SearchRequest();
        request.setQuery(query);
        request.setPage(page);
        request.setSize(size);
        request.setSources(sources);
        request.setFacets(facets);

        return search(request);
    }
} 