package com.mcp.springai_playground.sec07.servers.city.tools;

import com.mcp.springai_playground.sec07.servers.city.dto.CityResponse;
import com.mcp.springai_playground.sec07.servers.city.dto.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.ai.mcp.annotation.context.McpSyncRequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CityTools {

    private static final Logger log = LoggerFactory.getLogger(CityTools.class);

    private Map<String, String> cityCodeToNameMap;
    private Map<String, Recommendation> cityCodeToRecommendationMap;

    public CityTools(JsonMapper jsonMapper, @Value("classpath:${section}/city-data.json") Resource resource) throws IOException {
        initialize(jsonMapper, resource);
    }

    @McpTool(description = "Get city name and airport code mapping")
    public Map<String, String> getAirportCodesAndCityNames(McpSyncRequestContext context) {
        context.progress(spec -> spec.message("Fetching airport codes"));
        return cityCodeToNameMap;
    }

    @McpTool(description = "Get Local Recommendations by airportcode")
    public Recommendation getRecommendation(
            McpSyncRequestContext context,
            @McpToolParam(description = "airport code (e.g., JFK)") String airportCode) {
        context.progress(spec -> spec.message("Fetching recommendations for airport code: %s".formatted(airportCode)));
        return cityCodeToRecommendationMap.get(airportCode);
    }

    private void initialize(JsonMapper jsonMapper, Resource resource) throws IOException {

        var cityResponses = jsonMapper.readValue(resource.getFilePath(), new TypeReference<List<CityResponse>>() {});

        cityCodeToNameMap = cityResponses.stream()
                .collect(Collectors.toMap(
                        CityResponse::airportCode,
                        CityResponse::name
                ));

        cityCodeToRecommendationMap = cityResponses.stream()
                .collect(Collectors.toMap(
                        CityResponse::airportCode,
                        CityResponse::recommendation
                ));
    }


}
