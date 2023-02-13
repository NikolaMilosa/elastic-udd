package com.udd.elastic.validator;

import org.springframework.stereotype.Service;

@Service
public class EducationValidator {
    
    public boolean validateEducation(String education){
        return (education.equals("primary") || 
        education.equals("lower-secondary") ||
        education.equals("upper-secondary") ||
        education.equals("post-secondary") ||
        education.equals("bachelor") ||
        education.equals("master") || 
        education.equals("phd"));
    }
}
