package com.mcp.springai_playground.sec06.servers.flight.dto;

import java.time.LocalDateTime;

public record SearchResponse(String flightNumber,
                             String airline,
                             Integer price,
                             Integer flightDurationInMinutes,
                             String originAirportCode,
                             String departureAirportCode,
                             LocalDateTime arrivalTime,
                             LocalDateTime departureTime) {
}
