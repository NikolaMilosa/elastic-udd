package com.udd.elastic.service;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

@Component
public class PDFHandlerService {
    
    public String getText(byte[] bytes){
        try {
            var parser = new PDFParser(new RandomAccessBuffer(bytes));
            parser.parse();
            return new PDFTextStripper().getText(parser.getPDDocument());
        } catch (Exception e){
            System.out.println("Error while parsing pdf");
        }
        return null;
    }
}
