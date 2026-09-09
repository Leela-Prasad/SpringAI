package com.mcp.springai_playground.sec07.host.controller;

import com.mcp.springai_playground.sec07.host.config.NotificationChannel;
import com.mcp.springai_playground.sec07.host.dto.ChatRequest;
import com.mcp.springai_playground.sec07.host.dto.McpSessionManifest;
import com.mcp.springai_playground.sec07.host.dto.UserNotification;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/mcp")
public class McpController {

    private final ChatClient chatClient;
    private final McpSessionManifest mcpSessionManifest;
    private final NotificationChannel notificationChannel;

    public McpController(ChatClient chatClient, McpSessionManifest mcpSessionManifest, NotificationChannel notificationChannel) {
        this.chatClient = chatClient;
        this.mcpSessionManifest = mcpSessionManifest;
        this.notificationChannel = notificationChannel;
    }

    @GetMapping("manifest")
    public McpSessionManifest mcpSessionManifest() {
        return this.mcpSessionManifest;
    }

    @PostMapping
    public Flux<String> chat(@RequestBody ChatRequest request, @RequestHeader("userId") String userId) {
        var context = Map.<String, Object>of(
                "userId", Integer.valueOf(userId),
                "progressToken", request.progressToken()
        );

        return chatClient.prompt(request.message())
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, userId))
                .system(spec -> spec.param("date", LocalDate.now()))
                .toolContext(context)
                .stream()
                .content();

    }

    @GetMapping(value = "notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserNotification> notification(@RequestParam String progressToken) {
        return notificationChannel.stream(progressToken);
    }

    /*@GetMapping(value = "notifications2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<UserNotification>> notification2(@RequestParam String progressToken) {
        return notificationChannel.stream(progressToken)
                .map(notification -> ServerSentEvent.<UserNotification>builder()
                        .data(notification)
                        .build());
    }*/
}
