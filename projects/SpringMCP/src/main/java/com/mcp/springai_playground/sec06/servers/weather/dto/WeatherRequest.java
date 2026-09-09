package com.mcp.springai_playground.sec06.servers.weather.dto;

import org.springframework.ai.mcp.annotation.McpToolParam;

import java.time.LocalDateTime;

public record WeatherRequest(@McpToolParam(description = "Airport code for the destination (e.g., JFK)") String airportCode,
                             @McpToolParam(description = "Destination Arrival date time in ISO Format (e.g., 2026-01-25T17:00)") LocalDateTime arrivalTime) {
}
