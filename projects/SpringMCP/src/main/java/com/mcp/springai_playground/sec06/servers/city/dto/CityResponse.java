package com.mcp.springai_playground.sec06.servers.city.dto;

public record CityResponse(String airportCode,
                           String name,
                           Recommendation recommendation) {
}
