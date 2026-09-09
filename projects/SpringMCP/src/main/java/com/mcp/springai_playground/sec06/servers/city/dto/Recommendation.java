package com.mcp.springai_playground.sec06.servers.city.dto;

import java.util.List;

public record Recommendation(List<String> restaurants,
                             List<String> sightseeing) {
}
