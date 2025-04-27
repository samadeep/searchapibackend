package com.search.searchapi.config;

import com.search.searchapi.models.SearchItem;
import com.search.searchapi.repository.SearchItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MockDataLoader {

    @Bean
    public CommandLineRunner loadData(SearchItemRepository repository) {
        return args -> {
            // Create sample search items
            SearchItem item1 = createSearchItem("1", "Java Spring Boot Tutorial", 
                "Learn how to build REST APIs with Spring Boot", 
                "https://example.com/java-spring-boot", "GITHUB", 0.95);
            
            SearchItem item2 = createSearchItem("2", "Python Flask API", 
                "Building web APIs with Flask framework", 
                "https://example.com/python-flask", "STACKOVERFLOW", 0.88);
            
            SearchItem item3 = createSearchItem("3", "React vs Angular", 
                "Comparison of popular frontend frameworks", 
                "https://example.com/react-angular", "GOOGLE", 0.92);
            
            // Save items to database
            repository.saveAll(Arrays.asList(item1, item2, item3));
        };
    }

    private SearchItem createSearchItem(String id, String title, String description, 
                                      String url, String source, double score) {
        SearchItem item = new SearchItem();
        item.setId(id);
        item.setTitle(title);
        item.setDescription(description);
        item.setUrl(url);
        item.setSource(source);
        item.setScore(score);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("language", "java");
        metadata.put("stars", 1000);
        metadata.put("type", "tutorial");
        item.setMetadata(metadata);
        
        return item;
    }
} 