package com.udd.elastic.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class BooleanQueryField {
    
    private String field;
    
    private String value;
    
    private String operator;

    private boolean phraze;
}
