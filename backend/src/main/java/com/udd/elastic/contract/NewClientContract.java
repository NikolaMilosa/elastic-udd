package com.udd.elastic.contract;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NewClientContract implements Serializable {
    
    private String firstname;
    
    private String lastname;
    
    private String email;

    private String address;

    private MultipartFile cv;

    private MultipartFile letter;

    private String phone;
    
    private String education;

    public String stringify() {
        return firstname + ", " + lastname + ", " + email + ", " + address + ", " + cv.getSize() + ", " + letter.getSize() + ", " + phone + ", " + education;
    }
}
