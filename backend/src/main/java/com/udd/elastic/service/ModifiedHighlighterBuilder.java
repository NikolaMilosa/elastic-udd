package com.udd.elastic.service;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;

@Service
public class ModifiedHighlighterBuilder {
    
    public HighlightBuilder getBuilder(String field) {
        return new HighlightBuilder()
            .highlighterType("plain")
            .field(field)
            .preTags(getPreTag())
            .postTags(getPostTag());
    } 

    public String getPreTag() {
        return "<span style='background-color: yellow'>";
    }
    public String getPostTag() {
        return "</span>";
    }
}
