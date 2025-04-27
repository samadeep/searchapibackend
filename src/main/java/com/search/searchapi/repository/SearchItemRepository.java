package com.search.searchapi.repository;

import com.search.searchapi.models.SearchItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchItemRepository extends JpaRepository<SearchItem, String> {
} 