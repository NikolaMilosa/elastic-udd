package com.udd.elastic.service;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.elasticsearch.core.List;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.udd.elastic.contract.StatisticsContract;
import com.udd.elastic.model.Log;


@Service
public class StatisticsService {
    
    private final ElasticsearchOperations elasticsearchOperations;

    public StatisticsService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public StatisticsContract getStatistics() {
        return StatisticsContract.builder()
            .agentStats(getAgentStats())
            .cityStats(getCityStats())
            .companyStats(getCompanyStats())
            .build();
    }

    public Map<String, Long> getCityStats() {
        return getStats("city", null);
    }

    public Map<String, Long> getAgentStats() {
        return getStats("agent", QueryBuilders.matchQuery("outcome", "successful"));
    }

    public Map<String, Long> getCompanyStats() {
        return getStats("company", QueryBuilders.matchQuery("outcome", "successful"));
    }

    private Map<String, Long> getStats(String field, MatchQueryBuilder queryBuilder){
        var aggName = field + "_terms";
        var aggregation = AggregationBuilders
            .terms(aggName)
            .field(field + ".keyword")
            .size(Integer.MAX_VALUE);
        
        var query = new NativeSearchQueryBuilder()
            .withQuery(queryBuilder)
            .withAggregations(List.of(aggregation))
            .build();

        var result = (Aggregations) elasticsearchOperations.search(query, Log.class).getAggregations().aggregations();
        return getAggregations(result, aggName);
    }

    private Map<String, Long> getAggregations(Aggregations aggregations, String name) {
        if (aggregations != null) {
            var cities = aggregations.get(name);
            if (cities != null){
                var buckets = ((Terms) cities).getBuckets();
                if (buckets != null) {
                    return buckets.stream().collect(Collectors.toMap(Terms.Bucket::getKeyAsString, Terms.Bucket::getDocCount));
                }
            }
        }
        return Collections.emptyMap();
    };
}
