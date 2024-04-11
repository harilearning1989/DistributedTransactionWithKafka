package com.web.demo.records;

public record CustomerOrder(
        int orderId,
        String name,
        String item,
        int quantity,
        double amount,
        String paymentMethod,
        String address
) {
}
