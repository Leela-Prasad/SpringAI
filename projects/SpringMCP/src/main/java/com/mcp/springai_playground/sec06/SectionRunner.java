package com.mcp.springai_playground.sec06;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class SectionRunner {

    @SpringBootApplication(scanBasePackages = "com.mcp.springai_playground.${section}.host")
    static class Host {
        public static void main(String[] args) {
            SpringApplication.run(Host.class, "--section=sec06", "--config=host");
        }
    }

    @SpringBootApplication(scanBasePackages = "com.mcp.springai_playground.${section}.servers.city")
    static class CityServer {
        public static void main(String[] args) {
            SpringApplication.run(CityServer.class, "--section=sec06", "--config=city-service");
        }
    }

    @SpringBootApplication(scanBasePackages = "com.mcp.springai_playground.${section}.servers.flight")
    static class FlightServer {
        public static void main(String[] args) {
            SpringApplication.run(FlightServer.class, "--section=sec06", "--config=flight-service");
        }
    }

    @SpringBootApplication(scanBasePackages = "com.mcp.springai_playground.${section}.servers.user")
    static class UserServer {
        public static void main(String[] args) {
            SpringApplication.run(UserServer.class, "--section=sec06", "--config=user-service");
        }
    }

    @SpringBootApplication(scanBasePackages = "com.mcp.springai_playground.${section}.servers.weather")
    static class WeatherServer {
        public static void main(String[] args) {
            SpringApplication.run(WeatherServer.class, "--section=sec06", "--config=weather-service");
        }
    }

}
