package com.udd.elastic.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class SimpleQueryService {
    
    final ElasticsearchOperations elasticsearchOperations;
    final ModifiedHighlighterBuilder highlighterBuilder;

    public SimpleQueryService(ElasticsearchOperations operations, ModifiedHighlighterBuilder highlighterBuilder) {
        this.elasticsearchOperations = operations;
        this.highlighterBuilder = highlighterBuilder;
    }
    
    public <T> List<T> query(Class<T> type, String field, String value, int page) {
        
        var searchQuery = new NativeSearchQueryBuilder()
            .withFilter(new RegexpQueryBuilder(field.toLowerCase(), ".*" + value.toLowerCase() + ".*"))
            .withPageable(PageRequest.of(page, 10))
            .withHighlightBuilder(field.toLowerCase().equals("content") ? highlighterBuilder.getBuilder(field.toLowerCase()) : null)
            .build();

        var hints = elasticsearchOperations.search(searchQuery, type, IndexCoordinates.of(type.getSimpleName().toLowerCase()));
        var found = new ArrayList<T>();

        for (var hint : hints){
            found.add(hint.getContent());
        }

        return found;
    }
}
