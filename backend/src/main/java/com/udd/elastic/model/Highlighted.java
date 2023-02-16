package com.udd.elastic.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public abstract class Highlighted {

    @Field(type = FieldType.Text, searchAnalyzer = "serbian", analyzer = "serbian", fielddata = true)
    private String content;
}
