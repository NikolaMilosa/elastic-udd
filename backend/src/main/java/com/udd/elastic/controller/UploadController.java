package com.udd.elastic.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udd.elastic.contract.NewClientContract;
import com.udd.elastic.model.CV;
import com.udd.elastic.model.Client;
import com.udd.elastic.model.Letter;
import com.udd.elastic.repository.CVRepository;
import com.udd.elastic.repository.ClientRepository;
import com.udd.elastic.repository.LetterRepository;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    
    final ClientRepository clientRepository;
    final CVRepository cvRepository;
    final LetterRepository letterRepository;

    @Autowired
    public UploadController(ClientRepository clientRepository, CVRepository cvRepository,
            LetterRepository letterRepository) {
        this.clientRepository = clientRepository;
        this.cvRepository = cvRepository;
        this.letterRepository = letterRepository;
    }

    @PostMapping
    public ResponseEntity<?> upload(@ModelAttribute NewClientContract contract) throws IOException {
        if (!validateContract(contract)){
            return ResponseEntity.badRequest().body("Invalid request");
        }

        var cv = Base64.getEncoder().encodeToString(contract.getCv().getBytes());
        var letter = Base64.getEncoder().encodeToString(contract.getLetter().getBytes());

        var client = new Client();
        client.setFirstname(contract.getFirstname());
        client.setLastname(contract.getLastname());
        client.setAddress(contract.getAddress());
        client.setEmail(contract.getEmail());

        clientRepository.save(client);

        var cvObject = new CV();
        cvObject.setClient(client);
        cvObject.setContent(cv);

        cvRepository.save(cvObject);

        var letterObject = new Letter();
        letterObject.setClient(client);
        letterObject.setContent(letter);

        letterRepository.save(letterObject);
        

        return ResponseEntity.ok("successful");
    }

    private boolean validateContract(NewClientContract contract){
        return !(contract.getFirstname() == null || contract.getFirstname().isBlank() ||
        contract.getLastname() == null || contract.getLastname().isBlank() ||
        contract.getEmail() == null || contract.getEmail().isBlank() ||
        contract.getAddress() == null || contract.getAddress().isBlank() ||
        contract.getCv() == null || contract.getCv().getSize() == 0 ||
        contract.getLetter() == null || contract.getLetter().getSize() == 0);
    }
}
