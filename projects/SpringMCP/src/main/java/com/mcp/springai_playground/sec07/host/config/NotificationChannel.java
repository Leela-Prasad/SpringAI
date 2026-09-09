package com.mcp.springai_playground.sec07.host.config;

import com.mcp.springai_playground.sec07.host.dto.UserNotification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

public class NotificationChannel {

    private final Sinks.Many<UserNotification> sink;
    private final Flux<UserNotification> flux;

    public NotificationChannel(Sinks.Many<UserNotification> sink, Flux<UserNotification> flux) {
        this.sink = sink;
        this.flux = flux;
    }

    public void emit(UserNotification notification) {
        sink.emitNext(notification, Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1)));
    }

    public Flux<UserNotification> stream(String progressToken) {
        return flux.filter(notification -> notification.progressToken().equals(progressToken));
    }
}
