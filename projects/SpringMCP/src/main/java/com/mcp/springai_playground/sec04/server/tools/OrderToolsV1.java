package com.mcp.springai_playground.sec04.server.tools;

import com.mcp.springai_playground.sec04.server.dto.Order;
import com.mcp.springai_playground.sec04.server.service.OrderService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "version", havingValue = "v1")
public class OrderToolsV1 {

    private final OrderService orderService;

    public OrderToolsV1(OrderService orderService) {
        this.orderService = orderService;
    }

    @McpTool(description = "List all Orders")
    public List<Order> listOrders() {
        return orderService.listOrders();
    }

    @McpTool(description = "Check if an order is eligible for cancellation")
    public boolean isOrderCancellable(int orderId) {
        return orderService.isOrderCancellable(orderId);
    }

    @McpTool(description = "Cancel an order")
    public void cancelOrder(int orderId) {
        orderService.cancelOrder(orderId);
    }
}
