package com.mcp.springai_playground.sec07.servers.user.dto;

public record UserProfile(Integer userId,
                          String name,
                          Address address,
                          FlightPreference flightPreference) {
}
