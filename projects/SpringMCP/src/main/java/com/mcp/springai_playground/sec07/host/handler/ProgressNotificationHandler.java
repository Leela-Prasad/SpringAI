package com.mcp.springai_playground.sec07.host.handler;

import com.mcp.springai_playground.sec07.host.config.NotificationChannel;
import com.mcp.springai_playground.sec07.host.dto.UserNotification;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpProgress;
import org.springframework.stereotype.Component;

@Component
public class ProgressNotificationHandler {

    private static final Logger log = LoggerFactory.getLogger(ProgressNotificationHandler.class);
    private final NotificationChannel notificationChannel;

    public ProgressNotificationHandler(NotificationChannel notificationChannel) {
        this.notificationChannel = notificationChannel;
    }

    @McpProgress(clients = {"city-service",
                            "flight-service",
                            "user-service",
                            "weather-service"})
    public void handleProgressNotification(McpSchema.ProgressNotification notification) {
        log.info("Handling Notification {}", notification);
        var userNotification = new UserNotification(
                notification.progressToken().toString(),
                notification.message()
        );

        notificationChannel.emit(userNotification);
    }
}
