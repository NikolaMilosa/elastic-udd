package com.udd.elastic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udd.elastic.contract.LogContract;

@RestController
@RequestMapping("/api/log")
public class LogController {
    
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @PostMapping
    public ResponseEntity<?> log(@RequestBody LogContract request) {
        var logline = new StringBuilder()
            .append("City: ")
            .append(request.getCity())
            .append(", Agent: ")
            .append(request.getAgent())
            .append(", Outcome: ")
            .append(request.isSuccessful() ? "successful" : "unsuccessful")
            .append(", Company: ")
            .append(request.getCompany())
            .toString();

        logger.info(logline);

        return ResponseEntity.ok().build();
    }
}
