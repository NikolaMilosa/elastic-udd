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
import com.udd.elastic.validator.EducationValidator;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    
    final ClientRepository clientRepository;
    final CVRepository cvRepository;
    final LetterRepository letterRepository;
    final EducationValidator educationValidator;

    @Autowired
    public UploadController(ClientRepository clientRepository, CVRepository cvRepository,
            LetterRepository letterRepository, EducationValidator validator) {
        this.clientRepository = clientRepository;
        this.cvRepository = cvRepository;
        this.letterRepository = letterRepository;
        this.educationValidator = validator;
    }

    @PostMapping
    public ResponseEntity<?> upload(@ModelAttribute NewClientContract contract) throws IOException {
        if (!validateContract(contract)){
            return ResponseEntity.badRequest().body("Invalid request");
        }

        if (!educationValidator.validateEducation(contract.getEducation().toLowerCase())){
            return ResponseEntity.badRequest().body("Invalid education");
        }

        var cv = Base64.getEncoder().encodeToString(contract.getCv().getBytes());
        var letter = Base64.getEncoder().encodeToString(contract.getLetter().getBytes());

        var client = new Client();
        client.setFirstname(contract.getFirstname().trim().toLowerCase());
        client.setLastname(contract.getLastname().trim().toLowerCase());
        client.setAddress(contract.getAddress().trim());
        client.setEmail(contract.getEmail().trim());
        client.setPhone(contract.getPhone().trim());
        client.setEducation(contract.getEducation().trim().toLowerCase());

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
        contract.getLetter() == null || contract.getLetter().getSize() == 0 ||
        contract.getPhone() == null || contract.getPhone().isBlank() ||
        contract.getEducation() == null || contract.getEducation().isBlank());
    }
}
