package com.mcp.springai_playground.sec04.server.service;

import com.mcp.springai_playground.sec04.server.dto.Order;
import com.mcp.springai_playground.sec04.server.exception.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final int CANCELLATION_WINDOW_DAYS = 7;
    private final List<Order> orders = List.of(
            new Order(1, "Iphone", 990, LocalDate.now().minusDays(10)),
            new Order(2, "Ipad", 500, LocalDate.now().minusDays(5)),
            new Order(3, "Macbook", 2500, LocalDate.now().minusDays(1))
    );


    public List<Order> listOrders() {
        log.info("Listing Orders");
        return orders;
    }

    public boolean isOrderCancellable(int orderId) {
        log.info("Checking if order {} is eligible for cancellation", orderId);
        var order = findById(orderId);
        return LocalDate.now().minusDays(CANCELLATION_WINDOW_DAYS)
                .isBefore(order.orderDate());
    }

    public void cancelOrder(int orderId) {
        log.info("Cancelling order {}", orderId);
        // Cancellation Logic
    }

    private Order findById(int orderId) {
        return orders.stream()
                .filter(order -> order.orderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }


}
