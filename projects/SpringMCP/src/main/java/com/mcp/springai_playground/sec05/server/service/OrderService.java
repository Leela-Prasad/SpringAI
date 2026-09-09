package com.mcp.springai_playground.sec05.server.service;

import com.mcp.springai_playground.sec05.server.dto.Order;
import com.mcp.springai_playground.sec05.server.exception.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final Map<Integer, List<Order>> orders = Map.of(
            1, List.of(
                    new Order(1, "Iphone", 990, LocalDate.now().minusDays(10)),
                    new Order(2, "Ipad", 500, LocalDate.now().minusDays(5)),
                    new Order(3, "Macbook", 2500, LocalDate.now().minusDays(1))),
            2, List.of(
                    new Order(35, "Pixel", 990, LocalDate.now().minusDays(10)),
                    new Order(36, "Sony TV", 500, LocalDate.now().minusDays(2)))
    );


    public List<Order> listOrders(int userId) {
        log.info("Listing Orders for user: {}", userId);
        return orders.getOrDefault(userId, Collections.emptyList());
    }

    public void cancelOrder(int userId, int orderId) {
        log.info("Cancelling order {} for user {}", orderId, userId);
        var order = findByUserIdAndOrderId(userId, orderId);
        // Cancellation Logic
    }

    private Order findByUserIdAndOrderId(int userId, int orderId) {
        return orders.get(userId)
                .stream()
                .filter(order -> order.orderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }


}
