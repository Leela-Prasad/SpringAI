package com.mcp.springai_playground.sec05.server.tools;

import com.mcp.springai_playground.sec05.dto.UserCategory;
import com.mcp.springai_playground.sec05.dto.UserContext;
import com.mcp.springai_playground.sec05.server.dto.Order;
import com.mcp.springai_playground.sec05.server.dto.ToolResult;
import com.mcp.springai_playground.sec05.server.exception.OrderNotFoundException;
import com.mcp.springai_playground.sec05.server.service.OrderService;
import org.springframework.ai.mcp.annotation.McpMeta;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Component
public class OrderTools {

    private final OrderService orderService;
    private final JsonMapper jsonMapper;

    public OrderTools(OrderService orderService, JsonMapper jsonMapper) {
        this.orderService = orderService;
        this.jsonMapper = jsonMapper;
    }

    @McpTool(description = "List all Orders")
    public List<Order> listOrders(McpMeta meta) {
        var userContext = getUserContext(meta);
        return orderService.listOrders(userContext.userId());
    }

    @McpTool(description = "Cancel an order")
    public ToolResult cancelOrder(McpMeta meta, int orderId) {
        try {
            var userContext = getUserContext(meta);
            if(UserCategory.PREMIUM.equals(userContext.userCategory())) {
                orderService.cancelOrder(userContext.userId(), orderId);
                return new ToolResult("Order cancelled successfully");
            }

            return new ToolResult("Order Cancellation is allowed for premium users");
        } catch (OrderNotFoundException e) {
            return new ToolResult(e.getMessage());
        }
    }

    private UserContext getUserContext(McpMeta meta) {
        return jsonMapper.convertValue(meta.get("userContext"), UserContext.class);
    }
}
