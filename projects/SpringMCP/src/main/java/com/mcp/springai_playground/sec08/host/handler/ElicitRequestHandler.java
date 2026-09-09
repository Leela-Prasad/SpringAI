package com.mcp.springai_playground.sec08.host.handler;

import com.mcp.springai_playground.sec08.host.config.NotificationChannel;
import com.mcp.springai_playground.sec08.host.dto.UserNotification;
import com.mcp.springai_playground.sec08.host.dto.UserNotificationResponse;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpElicitation;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Component
public class ElicitRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(ElicitRequestHandler.class);
    private final NotificationChannel<UserNotification> notificationChannel;
    private final NotificationChannel<UserNotificationResponse> notificationChannelResponse;

    public ElicitRequestHandler(NotificationChannel<UserNotification> notificationChannel, NotificationChannel<UserNotificationResponse> notificationChannelResponse) {
        this.notificationChannel = notificationChannel;
        this.notificationChannelResponse = notificationChannelResponse;
    }

    @McpElicitation(clients = {"file-service"})
    public McpSchema.ElicitResult handleElicitRequest(McpSchema.ElicitFormRequest request) {
        log.info("Elicit Request Handling, request: {}", request);

        var progressToken = request.progressToken().toString();
        //notify user
        var notification = new UserNotification(progressToken,
                                                request.message(),
                                                request.requestedSchema());
        notificationChannel.emit(notification);

        // Wait for user response
        // This call blocks the current thread. Use virtual threads for scalability.
        // spring.threads.virtual.enabled=true
        return notificationChannelResponse.stream(progressToken)
                .next()
                .map(response -> toElicitResult(response.confirmed(), response.inputData()))
                .timeout(Duration.ofSeconds(50))
                .onErrorReturn(toElicitResult(false, Collections.emptyMap()))
                .block();
    }

    private McpSchema.ElicitResult toElicitResult(boolean confirmed, Map<String, Object> inputData) {
        var action = confirmed? McpSchema.ElicitResult.Action.ACCEPT: McpSchema.ElicitResult.Action.DECLINE;
        return McpSchema.ElicitResult.builder(action)
                .content(inputData)
                .build();
    }
}
