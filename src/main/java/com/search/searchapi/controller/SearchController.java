package com.search.searchapi.controller;

import com.search.searchapi.models.SearchRequest;
import com.search.searchapi.models.SearchResult;
import com.search.searchapi.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@Validated
@Tag(name = "Search", description = "Search API endpoints")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @Operation(summary = "Search across multiple sources")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = SearchResult.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "429", description = "Rate limit exceeded"),
        @ApiResponse(responseCode = "503", description = "One or more sources unavailable")
    })
    @PostMapping
    public ResponseEntity<SearchResult> search(@Valid @RequestBody SearchRequest request) {
        logger.info("Received search request: {}", request);
        SearchResult result = searchService.search(request);
        logger.info("Search completed in {}ms", result.getExecutionTime());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Search using query parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = SearchResult.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "429", description = "Rate limit exceeded"),
        @ApiResponse(responseCode = "503", description = "One or more sources unavailable")
    })
    @GetMapping
    public ResponseEntity<SearchResult> search(
            @Parameter(description = "Search query string", required = true, example = "spring boot tutorial")
            @NotBlank @Size(min = 2, max = 500) @RequestParam String query,
            
            @Parameter(description = "Page number (1-based)", example = "1")
            @Min(1) @RequestParam(defaultValue = "1") int page,
            
            @Parameter(description = "Results per page", example = "10")
            @Min(1) @Max(100) @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sources to search from", example = "GITHUB,STACKOVERFLOW")
            @RequestParam(required = false) List<String> sources,
            
            @Parameter(description = "Fields to generate facets for", example = "language,type")
            @RequestParam(required = false) List<String> facets) {
        
        logger.info("Received GET search request - query: {}, page: {}, size: {}, sources: {}, facets: {}",
                   query, page, size, sources, facets);
        
        SearchRequest request = new SearchRequest();
        request.setQuery(query);
        request.setPage(page);
        request.setSize(size);
        request.setSources(sources);
        request.setFacets(facets);

        SearchResult result = searchService.search(request);
        logger.info("Search completed in {}ms", result.getExecutionTime());
        return ResponseEntity.ok(result);
    }
} 