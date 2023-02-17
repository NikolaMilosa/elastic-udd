package com.udd.elastic.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class LocationContract {
    
    private Double lat;

    private Double lon;
}
