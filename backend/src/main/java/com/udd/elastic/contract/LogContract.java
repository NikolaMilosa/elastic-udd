package com.udd.elastic.contract;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LogContract {
    
    private String city;
    private String agent;
    private boolean successful;
    private String company;
}
