package com.udd.elastic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.udd.elastic.model.CV;
import com.udd.elastic.model.Client;
import com.udd.elastic.model.Letter;
import com.udd.elastic.repository.CVRepository;
import com.udd.elastic.repository.LetterRepository;

@Service
public class IndexerService {
    final PDFHandlerService pdfHandlerService;
    final CVRepository cvRepository;
    final LetterRepository letterRepository;

    @Autowired
    public IndexerService(PDFHandlerService pdfHandlerService, CVRepository cvRepository, LetterRepository letterRepository) {
        this.pdfHandlerService = pdfHandlerService;
        this.cvRepository = cvRepository;
        this.letterRepository = letterRepository;
    }

    public boolean add(Client client, MultipartFile cvFile, MultipartFile letterFile) {
        var cvContent = "";
        var letterContent = "";
        try {
            cvContent = pdfHandlerService.getText(cvFile.getBytes());
            letterContent = pdfHandlerService.getText(letterFile.getBytes());    
        } catch (Exception e){
            return false;
        }
        
        if (cvContent == null || cvContent.isBlank() ||
            letterContent == null || letterContent.isBlank()){
            return false;
        }

        var cv = new CV();
        cv.setClient(client);
        cv.setContent(cvContent);
        cvRepository.save(cv);

        var letter = new Letter();
        letter.setClient(client);
        letter.setContent(letterContent);
        letterRepository.save(letter);

        return true;
    }
}
