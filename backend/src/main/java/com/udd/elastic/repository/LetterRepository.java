package com.udd.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.udd.elastic.model.Letter;

@Repository
public interface LetterRepository extends ElasticsearchRepository<Letter, String> {
}
