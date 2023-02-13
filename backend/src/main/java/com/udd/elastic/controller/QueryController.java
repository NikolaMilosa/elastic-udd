package com.udd.elastic.controller;

import java.util.ArrayList;

import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udd.elastic.model.Client;
import com.udd.elastic.validator.EducationValidator;

@RestController
@RequestMapping("/api/query")
public class QueryController {
    
    final ElasticsearchOperations elasticsearchOperations;
    final EducationValidator educationValidator;

    @Autowired
    public QueryController(ElasticsearchOperations operations, EducationValidator validator) {
        this.elasticsearchOperations = operations;
        this.educationValidator = validator;
    }
    
    @GetMapping("/firstname/{firstname}/{page}")
    public Iterable<Client> queryFirstname(@PathVariable(name = "firstname") String firstname,
        @PathVariable(name = "page") int page) {

        var searchQuery = new NativeSearchQueryBuilder()
            .withFilter(new RegexpQueryBuilder("firstname", ".*" + firstname.trim().toLowerCase() + ".*"))
            .build();
        
        var clients = elasticsearchOperations.search(searchQuery, Client.class);
        var found = new ArrayList<Client>();

        for (var hint : clients.getSearchHits()){
            System.out.println("Found");
            found.add(hint.getContent());
        }

        return found;
    }
    
    @GetMapping("/lastname/{lastname}/{page}")
    public Iterable<Client> queryLastname(@PathVariable(name = "lastname") String lastname,
        @PathVariable(name = "page") int page) {
        
        var searchQuery = new NativeSearchQueryBuilder()
            .withFilter(new RegexpQueryBuilder("lastname", ".*" + lastname.trim().toLowerCase() + ".*"))
            .build();
        
        var clients = elasticsearchOperations.search(searchQuery, Client.class);
        var found = new ArrayList<Client>();

        for (var hint : clients.getSearchHits()){
            found.add(hint.getContent());
        }

        return found;
    }

    @GetMapping("/education/{education}/{page}")
    public ResponseEntity<?> queryEducation(@PathVariable(name = "education") String education,
        @PathVariable(name = "page") int page) {
        
        if (!educationValidator.validateEducation(education.trim().toLowerCase())){
            return ResponseEntity.badRequest().body("Invalid education");
        }

        var searchQuery = new NativeSearchQueryBuilder()
            .withFilter(new RegexpQueryBuilder("education", education.trim().toLowerCase()))
            .build();
        
        var clients = elasticsearchOperations.search(searchQuery, Client.class);
        var found = new ArrayList<Client>();

        for (var hint : clients.getSearchHits()){
            found.add(hint.getContent());
        }

        return ResponseEntity.ok(found);
    }
}
