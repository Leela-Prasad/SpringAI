package com.mcp.springai_playground.sec06.servers.weather.tools;

import com.mcp.springai_playground.sec06.servers.weather.dto.WeatherRequest;
import com.mcp.springai_playground.sec06.servers.weather.dto.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class WeatherTools {

    private static final Logger log = LoggerFactory.getLogger(WeatherTools.class);
    private final List<String> CONDITIONS = List.of(
            "Sunny", "Partly Cloudy", "Cloudy", "Rainy", "Windy", "Clear", "Scattered Thunderstorms"
    );

    @McpTool(description = "Get weather forecast for a specific airport and time")
    public WeatherResponse getWeatherForecast(WeatherRequest weatherRequest) {
        log.info("fetching weather forcest for {}", weatherRequest);
        var temperature = ThreadLocalRandom.current().nextInt(50, 101); //Farenheit
        var condition = CONDITIONS.get(ThreadLocalRandom.current().nextInt(CONDITIONS.size()));
        return new WeatherResponse(temperature, condition);
    }
}
