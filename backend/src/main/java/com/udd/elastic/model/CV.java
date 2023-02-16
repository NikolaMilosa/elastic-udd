package com.udd.elastic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "cv")
@Data
@NoArgsConstructor
public class CV extends Highlighted{
    @Id
    private String id;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Client client;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private Long cvId;
}
