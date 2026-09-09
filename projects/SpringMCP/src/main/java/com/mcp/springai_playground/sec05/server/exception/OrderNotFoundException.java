package com.mcp.springai_playground.sec05.server.exception;

public class OrderNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Order Id %s is not found";

    public OrderNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }

}
