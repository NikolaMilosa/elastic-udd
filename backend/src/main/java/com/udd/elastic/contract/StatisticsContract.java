package com.udd.elastic.contract;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsContract {
    @JsonProperty("city_stats")
    private Map<String, Long> cityStats;
    @JsonProperty("agent_stats")
    private Map<String, Long> agentStats;
    @JsonProperty("company_stats")
    private Map<String, Long> companyStats;
}
