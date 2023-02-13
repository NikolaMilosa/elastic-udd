package com.udd.elastic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.udd.elastic.model.Client;

@Repository
public interface ClientRepository extends ElasticsearchRepository<Client, String>{

    Page<Client> findByFirstname(String firstname, Pageable pageable);

    Page<Client> findByLastname(String lastname, Pageable pageable);
}
