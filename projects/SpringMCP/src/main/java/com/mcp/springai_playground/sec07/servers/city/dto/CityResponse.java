package com.mcp.springai_playground.sec07.servers.city.dto;

public record CityResponse(String airportCode,
                           String name,
                           Recommendation recommendation) {
}
