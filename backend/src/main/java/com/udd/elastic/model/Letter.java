package com.udd.elastic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "letter")
@Data
@NoArgsConstructor
public class Letter {
    @Id
    private String id;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Client client;

    @Field(type = FieldType.Text, analyzer = "english")
    private String content;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private Long letterId;
}
