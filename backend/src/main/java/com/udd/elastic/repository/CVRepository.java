package com.udd.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.udd.elastic.model.CV;

@Repository
public interface CVRepository extends ElasticsearchRepository<CV, String> {
}
