package com.mcp.springai_playground.sec04.server.tools;

import com.mcp.springai_playground.sec04.server.dto.Order;
import com.mcp.springai_playground.sec04.server.dto.ToolResult;
import com.mcp.springai_playground.sec04.server.exception.OrderNotFoundException;
import com.mcp.springai_playground.sec04.server.service.OrderService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "version", havingValue = "v3")
public class OrderToolsV3 {

    private final OrderService orderService;

    public OrderToolsV3(OrderService orderService) {
        this.orderService = orderService;
    }

    @McpTool(description = "List all Orders")
    public List<Order> listOrders() {
        return orderService.listOrders();
    }

    @McpTool(description = "Cancel an order")
    public ToolResult cancelOrder(int orderId) {
        try {
            if(orderService.isOrderCancellable(orderId)) {
                orderService.cancelOrder(orderId);
                return new ToolResult("Order cancelled successfully");
            }

            return new ToolResult("This order is not eligible for cancellation");
        } catch (OrderNotFoundException e) {
            return new ToolResult(e.getMessage());
        }
    }
}
