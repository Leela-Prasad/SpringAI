package com.mcp.springai_playground.sec06.servers.user.tools;

import com.mcp.springai_playground.sec06.servers.user.dto.Address;
import com.mcp.springai_playground.sec06.servers.user.dto.FlightPreference;
import com.mcp.springai_playground.sec06.servers.user.dto.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpMeta;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserTools {

    private static final Logger log = LoggerFactory.getLogger(UserTools.class);
    private final Map<Integer, UserProfile> userProfiles = Map.of(
            1, new UserProfile(1, "Sam", new Address("123 north street", "Atlanta", "Georgia"), FlightPreference.CHEAP_PRICE),
                2, new UserProfile(2, "Mike", new Address("456 south street", "San Francisco", "California"), FlightPreference.SHORT_DURATION)
    );

    @McpTool(description = "Get User Profiles and Travel preferences")
    public UserProfile getUserProfile(McpMeta meta) {
        var userId = (int) meta.get("userId");
        log.info("fetching user details for user id: {}", userId);
        return userProfiles.get(userId);
    }

}
