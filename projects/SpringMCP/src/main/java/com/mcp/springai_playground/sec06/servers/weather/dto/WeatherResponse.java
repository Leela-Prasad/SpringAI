package com.mcp.springai_playground.sec06.servers.weather.dto;

public record WeatherResponse(Integer temperature,
                              String condition) {
}
