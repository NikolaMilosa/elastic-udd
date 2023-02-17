package com.udd.elastic.service;

import java.util.ArrayList;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.udd.elastic.configuration.LocationHttpClient;
import com.udd.elastic.contract.LocationContract;
import com.udd.elastic.model.Client;
;

@Service
public class LocationQueryService {
    
    final ElasticsearchOperations elasticsearchOperations;
    final LocationHttpClient locationHttpClient;

    public LocationQueryService(ElasticsearchOperations elasticsearchOperations, LocationHttpClient locationHttpClient){
        this.elasticsearchOperations = elasticsearchOperations;
        this.locationHttpClient = locationHttpClient;
    }

    public ArrayList<Client> query(LocationContract location, Double radius, String unit, int page){
        var locationFilter = new GeoDistanceQueryBuilder("location")
            .point(location.getLat(), location.getLon())
            .distance(radius, DistanceUnit.parseUnit(unit, DistanceUnit.KILOMETERS));

        var query = new NativeSearchQueryBuilder()
            .withFilter(locationFilter)
            .withPageable(PageRequest.of(page, 10))
            .build();
        
        var hints = elasticsearchOperations.search(query, Client.class, IndexCoordinates.of("client"));
        var found = new ArrayList<Client>();
        for(var hint : hints) {
            found.add(hint.getContent());
        }

        return found;
    }
}
