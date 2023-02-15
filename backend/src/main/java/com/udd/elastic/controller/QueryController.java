package com.udd.elastic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udd.elastic.contract.AdvancedSearchRequest;
import com.udd.elastic.model.CV;
import com.udd.elastic.model.Client;
import com.udd.elastic.model.Letter;
import com.udd.elastic.service.AdvancedQueryService;
import com.udd.elastic.service.SimpleQueryService;
import com.udd.elastic.validator.EducationValidator;

@RestController
@RequestMapping("/api/query")
public class QueryController {
    
    final EducationValidator educationValidator;
    final SimpleQueryService simpleQueryService;
    final AdvancedQueryService advancedQueryService;

    @Autowired
    public QueryController(EducationValidator validator, SimpleQueryService simpleQueryService, AdvancedQueryService advancedQueryService) {
        this.educationValidator = validator;
        this.simpleQueryService = simpleQueryService;
        this.advancedQueryService = advancedQueryService;
    }
    
    @GetMapping("/firstname/{firstname}/{page}")
    public Iterable<Client> queryFirstname(@PathVariable(name = "firstname") String firstname,
        @PathVariable(name = "page") int page) {
        return simpleQueryService.query(Client.class, "firstname", firstname, page);
    }
    
    @GetMapping("/lastname/{lastname}/{page}")
    public Iterable<Client> queryLastname(@PathVariable(name = "lastname") String lastname,
        @PathVariable(name = "page") int page) {
        return simpleQueryService.query(Client.class, "lastname", lastname, page);
    }

    @GetMapping("/education/{education}/{page}")
    public ResponseEntity<?> queryEducation(@PathVariable(name = "education") String education,
        @PathVariable(name = "page") int page) {
        
        if (!educationValidator.validateEducation(education.trim().toLowerCase())){
            return ResponseEntity.badRequest().body("Invalid education");
        }

        return ResponseEntity.ok(simpleQueryService.query(Client.class, "education", education, page));
    }

    @GetMapping("/cv/{query}/{page}")
    public Iterable<CV> queryCv(@PathVariable(name = "query") String query, @PathVariable(name = "page") int page) {
        return simpleQueryService.query(CV.class, "content", query, page);
    }

    @GetMapping("/letter/{query}/{page}")
    public Iterable<Letter> queryLetter(@PathVariable(name = "query") String query, @PathVariable(name = "page") int page) {
        return simpleQueryService.query(Letter.class, "content", query, page);
    }

    @PostMapping("/advanced/{page}/cv")
    public ResponseEntity<?> queryAdvancedCV(@PathVariable(name = "page") int page, @RequestBody AdvancedSearchRequest request){
        if (!validateAdvancedRequest(request)){
            return ResponseEntity.badRequest().body("Invalid request");
        }

        return ResponseEntity.ok(advancedQueryService.query(CV.class, request, page));
    }

    @PostMapping("/advanced/{page}/letter")
    public ResponseEntity<?> queryAdvancedLetter(@PathVariable(name = "page") int page, @RequestBody AdvancedSearchRequest request){
        if (!validateAdvancedRequest(request)){
            return ResponseEntity.badRequest().body("Invalid request");
        }

        return ResponseEntity.ok(advancedQueryService.query(Letter.class, request, page));
    }

    private boolean validateAdvancedRequest(AdvancedSearchRequest request){
        if (request == null || request.query == null || request.query.isEmpty()){
            return false;
        }

        for (var booleanRequest : request.getQuery()){
            if (booleanRequest.getField() == null || booleanRequest.getField().isBlank() ||
                booleanRequest.getValue() == null || booleanRequest.getValue().isBlank() ||
                booleanRequest.getOperator() == null || booleanRequest.getOperator().isBlank() || (!booleanRequest.getOperator().toLowerCase().equals("and")) && !booleanRequest.getOperator().toLowerCase().equals("or")) {
                return false;
            }
        }

        return true;
    }
}
