package com.mcp.springai_playground.sec07.servers.weather.dto;

public record WeatherResponse(Integer temperature,
                              String condition) {
}
