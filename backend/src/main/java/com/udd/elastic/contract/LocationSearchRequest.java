package com.udd.elastic.contract;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationSearchRequest {
    
    private String city;

    private Double radius;

    private String unit;
}
