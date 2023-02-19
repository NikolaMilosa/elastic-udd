package com.udd.elastic.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
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
        var query = getQueryBuilder(request);

        var nativeQuery = getHighlightableQuery(query, page);

        var hints = elasticsearchOperations.search(nativeQuery, type, IndexCoordinates.of(type.getSimpleName().toLowerCase()));
        return getWithHighlighting(hints);
    }

    public <T extends Object> List<T> queryWithoutHighlihghitng(Class<T> type, AdvancedSearchRequest request, int page) {
        var query = getQueryBuilder(request);
        var nativeQuery = getNonHighlightableQuery(query, page);

        var hints = elasticsearchOperations.search(nativeQuery, type, IndexCoordinates.of(type.getSimpleName().toLowerCase()));
        return getWithoutHighlighting(hints);
    }

    private QueryBuilder getQueryBuilder(AdvancedSearchRequest request){
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
        return query;
    }

    private NativeSearchQuery getHighlightableQuery(QueryBuilder query, int page){
        return new NativeSearchQueryBuilder()
            .withQuery(query)
            .withHighlightBuilder(highlighterBuilder.getBuilder("content"))
            .withPageable(PageRequest.of(page, 10))
            .build();
    }

    private NativeSearchQuery getNonHighlightableQuery(QueryBuilder query, int page){
        return new NativeSearchQueryBuilder()
            .withQuery(query)
            .withPageable(PageRequest.of(page, 10))
            .build();
    }

    private <T extends Object> List<T> getWithoutHighlighting(SearchHits<T> hints){
        var ret = new ArrayList<T>();

        for(var hint : hints){
            var content = hint.getContent();
            ret.add(content);
        }

        return ret;
    }

    private <T extends Highlighted> List<T> getWithHighlighting(SearchHits<T> hints){
        var ret = new ArrayList<T>();

        for(var hint : hints){
            var content = hint.getContent();
            for (var highlight : hint.getHighlightFields().get("content")){
                var holder = highlight.replaceAll(highlighterBuilder.getPreTag(), "").replaceAll(highlighterBuilder.getPostTag(), "");
                content.setContent(content.getContent().replace(holder, highlight));
            }

            ret.add(content);
        }

        return ret;
    }
}
