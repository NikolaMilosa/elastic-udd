package com.udd.elastic.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "log")
@Data
@NoArgsConstructor
public class Log {
    
    @Field(type = FieldType.Text, searchAnalyzer = "serbian", analyzer = "serbian", store = true, fielddata = true)
    private String level;

    @Field(type = FieldType.Text, searchAnalyzer = "serbian", analyzer = "serbian", store = true, fielddata = true)
    private String agent;

    @Field(type = FieldType.Text, searchAnalyzer = "serbian", analyzer = "serbian", store = true, fielddata = true)
    private String company;

    @Field(type = FieldType.Text, searchAnalyzer = "english", analyzer = "english", store = true, fielddata = true)
    private String outcome;
    
    @Field(type = FieldType.Text, searchAnalyzer = "serbian", analyzer = "serbian", store = true, fielddata = true)
    private String city;
}
