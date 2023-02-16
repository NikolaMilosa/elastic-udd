package com.udd.elastic.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.udd.elastic.contract.AdvancedSearchRequest;
import com.udd.elastic.model.Highlighted;

@Service
public class AdvancedQueryService {
    
    final ElasticsearchOperations elasticsearchOperations;
    final ModifiedHighlighterBuilder highlighterBuilder;

    public AdvancedQueryService(ElasticsearchOperations operations, ModifiedHighlighterBuilder highlighterBuilder) {
        this.elasticsearchOperations = operations;
        this.highlighterBuilder = highlighterBuilder;
    }

    public <T extends Highlighted> List<T> query(Class<T> type, AdvancedSearchRequest request, int page) {
        
        var query = QueryBuilders.boolQuery();
        for (var field : request.getQuery()){
            QueryBuilder matcher = field.isPhraze() 
                ? QueryBuilders.matchPhraseQuery(field.getField(), field.getValue())
                : QueryBuilders.matchQuery(field.getField(), field.getValue());

            if (field.getOperator().toLowerCase().equals("and")) {
               query.must(matcher); 
            }
            else {
                query.should(matcher);
            }
        }

        var nativeQuery = new NativeSearchQueryBuilder()
            .withQuery(query)
            .withHighlightBuilder(highlighterBuilder.getBuilder("content"))
            .withPageable(PageRequest.of(page, 10))
            .build();

        var hints = elasticsearchOperations.search(nativeQuery, type, IndexCoordinates.of(type.getSimpleName().toLowerCase()));
        var found = new ArrayList<T>();

        for(var hint : hints){
            var content = hint.getContent();
            for (var highlight : hint.getHighlightFields().get("content")){
                var holder = highlight.replaceAll("<strong>", "").replaceAll("</strong>", "");
                content.setContent(content.getContent().replace(holder, highlight));
            }

            found.add(content);
        }

        return found;
    }
}
