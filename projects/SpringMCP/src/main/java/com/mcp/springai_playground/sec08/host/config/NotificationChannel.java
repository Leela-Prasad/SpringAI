package com.mcp.springai_playground.sec08.host.config;

import com.mcp.springai_playground.sec08.host.dto.NotificationEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

public class NotificationChannel<T extends NotificationEvent> {

    private final Sinks.Many<T> sink;
    private final Flux<T> flux;

    public NotificationChannel(Sinks.Many<T> sink, Flux<T> flux) {
        this.sink = sink;
        this.flux = flux;
    }

    public void emit(T notification) {
        System.out.println("Emit Entry :: " + notification);
//        sink.emitNext(notification, Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1)));
       var result = sink.tryEmitNext(notification);
        System.out.println("Emit Exit :: " + result);
    }

    public Flux<T> stream(String progressToken) {
        System.out.println("ProgressToken :: " + progressToken);
        return flux
                .doOnNext(event -> System.out.println("Event received :: " + event))
                .filter(notification -> notification.progressToken().equals(progressToken));
    }
}
