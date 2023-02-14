package com.udd.elastic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udd.elastic.contract.NewClientContract;
import com.udd.elastic.model.Client;
import com.udd.elastic.repository.ClientRepository;
import com.udd.elastic.service.IndexerService;
import com.udd.elastic.validator.EducationValidator;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    
    final ClientRepository clientRepository;
    final EducationValidator educationValidator;
    final IndexerService indexerService;

    @Autowired
    public UploadController(ClientRepository clientRepository, EducationValidator validator, IndexerService indexerService) {
        this.clientRepository = clientRepository;
        this.educationValidator = validator;
        this.indexerService = indexerService;
    }

    @PostMapping
    public ResponseEntity<?> upload(@ModelAttribute NewClientContract contract) {
        if (!validateContract(contract)){
            return ResponseEntity.badRequest().body("Invalid request");
        }

        if (!educationValidator.validateEducation(contract.getEducation().toLowerCase())){
            return ResponseEntity.badRequest().body("Invalid education");
        }

        var client = new Client();
        client.setFirstname(contract.getFirstname().trim().toLowerCase());
        client.setLastname(contract.getLastname().trim().toLowerCase());
        client.setAddress(contract.getAddress().trim());
        client.setEmail(contract.getEmail().trim());
        client.setPhone(contract.getPhone().trim());
        client.setEducation(contract.getEducation().trim().toLowerCase());

        clientRepository.save(client);        

        if (!indexerService.add(client, contract.getCv(), contract.getLetter())){
            clientRepository.delete(client);
            return ResponseEntity.badRequest().body("Failed to parse cv or letter");
        }

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
