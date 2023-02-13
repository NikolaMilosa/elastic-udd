package com.udd.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.udd.elastic.model.Client;

@Repository
public interface ClientRepository extends ElasticsearchRepository<Client, String>{
}
