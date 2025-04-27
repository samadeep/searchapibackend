package com.search.searchapi.models;

import com.search.searchapi.config.JsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

@Entity
@Table(name = "search_items", indexes = {
    @Index(name = "idx_search_items_source", columnList = "source"),
    @Index(name = "idx_search_items_score", columnList = "score")
})
public class SearchItem {
    @Id
    @NotBlank(message = "ID cannot be blank")
    private String id;
    
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @Column(nullable = false)
    private String title;
    
    @Size(max = 4000, message = "Description cannot exceed 4000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "URL cannot be blank")
    @Size(max = 2048, message = "URL cannot exceed 2048 characters")
    @Column(nullable = false)
    private String url;
    
    @NotBlank(message = "Source cannot be blank")
    @Column(nullable = false)
    private String source;
    
    @NotNull(message = "Score cannot be null")
    @Column(nullable = false)
    private double score;
    
    @Column(columnDefinition = "TEXT")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> metadata;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
} 