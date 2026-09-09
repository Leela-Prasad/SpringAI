package com.mcp.springai_playground.sec02.host.controller;

import com.mcp.springai_playground.sec02.host.dto.ChatRequest;
import com.mcp.springai_playground.sec02.host.dto.McpSessionManifest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/mcp")
public class McpController {

    private final ChatClient chatClient;
    private final McpSessionManifest mcpSessionManifest;

    public McpController(ChatClient chatClient, McpSessionManifest mcpSessionManifest) {
        this.chatClient = chatClient;
        this.mcpSessionManifest = mcpSessionManifest;
    }

    @GetMapping("manifest")
    public McpSessionManifest mcpSessionManifest() {
        return this.mcpSessionManifest;
    }

    @PostMapping
    public Flux<String> chat(@RequestBody ChatRequest request) {
        return chatClient.prompt(request.message())
                .stream()
                .content();

    }
}
