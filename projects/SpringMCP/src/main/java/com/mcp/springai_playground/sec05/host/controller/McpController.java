package com.mcp.springai_playground.sec05.host.controller;

import com.mcp.springai_playground.sec05.dto.UserCategory;
import com.mcp.springai_playground.sec05.dto.UserContext;
import com.mcp.springai_playground.sec05.host.dto.ChatRequest;
import com.mcp.springai_playground.sec05.host.dto.McpSessionManifest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Objects;

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
    public Flux<String> chat(@RequestBody ChatRequest request, @RequestHeader("userId") String uId) {
        Integer userId = Integer.valueOf(uId);
        var userType = (userId.equals(2)? UserCategory.PREMIUM: UserCategory.NORMAL);

        return chatClient.prompt(request.message())
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, userId))
                .toolContext(Map.of("userContext", new UserContext(userId, userType)))
                .stream()
                .content();

    }
}
