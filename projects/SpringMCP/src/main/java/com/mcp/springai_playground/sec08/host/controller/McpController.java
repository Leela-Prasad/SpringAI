package com.mcp.springai_playground.sec08.host.controller;

import com.mcp.springai_playground.sec08.host.config.NotificationChannel;
import com.mcp.springai_playground.sec08.host.dto.ChatRequest;
import com.mcp.springai_playground.sec08.host.dto.McpSessionManifest;
import com.mcp.springai_playground.sec08.host.dto.UserNotification;
import com.mcp.springai_playground.sec08.host.dto.UserNotificationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/api/mcp")
public class McpController {

    private final ChatClient chatClient;
    private final McpSessionManifest mcpSessionManifest;
    private final NotificationChannel<UserNotification> notificationChannel;
    private final NotificationChannel<UserNotificationResponse> notificationChannelResponse;

    public McpController(ChatClient chatClient, McpSessionManifest mcpSessionManifest, NotificationChannel<UserNotification> notificationChannel, NotificationChannel<UserNotificationResponse> notificationChannelResponse) {
        this.chatClient = chatClient;
        this.mcpSessionManifest = mcpSessionManifest;
        this.notificationChannel = notificationChannel;
        this.notificationChannelResponse = notificationChannelResponse;
    }

    @GetMapping("manifest")
    public McpSessionManifest mcpSessionManifest() {
        return this.mcpSessionManifest;
    }

    @PostMapping
    public Flux<String> chat(@RequestBody ChatRequest request) {
        return chatClient.prompt(request.message())
                .toolContext(Map.of("progressToken", request.progressToken()))
                .stream()
                .content();
    }

    @GetMapping(value = "notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserNotification> notification(@RequestParam String progressToken) {
        return notificationChannel.stream(progressToken);
    }

    @PostMapping("notifications")
    public void handleUserResponse(@RequestBody UserNotificationResponse userNotificationResponse) {
        notificationChannelResponse.emit(userNotificationResponse);
    }
}
