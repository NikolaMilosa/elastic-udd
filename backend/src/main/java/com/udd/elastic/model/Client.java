package com.udd.elastic.model;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "client")
@Data
@NoArgsConstructor
public class Client {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Text, searchAnalyzer = "english", analyzer = "english", store = true)
    private String firstname;

    @Field(type = FieldType.Text, searchAnalyzer = "english", analyzer = "english", store = true)
    private String lastname;

    @Field(type = FieldType.Text, index = false, store = true)
    private String email;

    @Field(type = FieldType.Text, index = false, store = true)
    private String address;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Text, index = false, store = true)
    private String phone;

    @Field(type = FieldType.Text, store = true)
    private String education;
}
