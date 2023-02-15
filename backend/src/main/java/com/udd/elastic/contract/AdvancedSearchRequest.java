package com.udd.elastic.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdvancedSearchRequest {
    
    public List<BooleanQueryField> query;
}
