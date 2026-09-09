package com.mcp.springai_playground.sec07.host.config;

import com.mcp.springai_playground.sec07.host.dto.McpSessionManifest;
import com.mcp.springai_playground.sec07.host.dto.UserNotification;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider,
                                 @Value("classpath:${section}/system-message.txt") Resource systemMessage) {

        var chatMemory = MessageWindowChatMemory.builder().maxMessages(20).build();
        var chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return builder.defaultSystem(systemMessage)
                .defaultAdvisors(spec -> spec.advisors(chatMemoryAdvisor))
//                        .param(ChatMemory.CONVERSATION_ID, "default"))
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    @Bean
    public McpSessionManifest mcpSessionManifest(ChatModel chatModel, ToolCallbackProvider toolCallbackProvider,
                                                 @Value("classpath:${section}/suggested-inputs.txt") Resource suggestedUserInputs) throws IOException {
        var modelName = chatModel.getOptions().getModel();
        List<String> tools = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                .map(toolCallback -> toolCallback.getToolDefinition().name())
                .toList();
        var suggestedInputs = Files.readAllLines(suggestedUserInputs.getFilePath());

        return new McpSessionManifest(modelName, tools, suggestedInputs);
    }

    @Bean
    public NotificationChannel notificationChannel() {
        var sink = Sinks.many().multicast().<UserNotification>onBackpressureBuffer();
        var flux = sink.asFlux();
        return new NotificationChannel(sink, flux);
    }
}
