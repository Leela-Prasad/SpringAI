package com.mcp.springai_playground.sec07.servers.flight.tools;

import com.mcp.springai_playground.sec07.servers.flight.dto.BookingRequest;
import com.mcp.springai_playground.sec07.servers.flight.dto.BookingResponse;
import com.mcp.springai_playground.sec07.servers.flight.dto.SearchRequest;
import com.mcp.springai_playground.sec07.servers.flight.dto.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.context.McpSyncRequestContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FlightTools {

    private static final Logger log = LoggerFactory.getLogger(FlightTools.class);

    @McpTool(description = "Search Flights for a given origin, destination and date")
    public List<SearchResponse> searchFlights(McpSyncRequestContext context, SearchRequest request) {
        context.progress(spec -> spec.message("Searching Flights, request: %s".formatted(request)));

        var cheapFlight = new SearchResponse(
            "AA" + ThreadLocalRandom.current().nextInt(100, 1000),
                "American Airlines",
                200,
                360,
                request.originAirportCode(),
                request.destinationAirportCode(),
                LocalDateTime.of(request.travelDate(), LocalTime.of(14, 0)),
                LocalDateTime.of(request.travelDate(), LocalTime.of(8, 0))
        );

        var shortFlight = new SearchResponse(
                "DL" + ThreadLocalRandom.current().nextInt(100, 1000),
                "Delta Airlines",
                500,
                100,
                request.originAirportCode(),
                request.destinationAirportCode(),
                LocalDateTime.of(request.travelDate(), LocalTime.of(13, 0)),
                LocalDateTime.of(request.travelDate(), LocalTime.of(10, 0))
        );

        return List.of(shortFlight, cheapFlight);
    }

    @McpTool(description = "Book flight for a passenger")
    public BookingResponse bookFlight(McpSyncRequestContext context, BookingRequest request) {
        context.progress(spec -> spec.message("Book flight, request: %s".formatted(request)));
        return new BookingResponse(UUID.randomUUID(),
                request.flightNumber(),
                "CONFIRMED");
    }
}
