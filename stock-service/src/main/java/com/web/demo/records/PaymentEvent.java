package com.web.demo.records;

public record PaymentEvent(
        CustomerOrder customerOrder,
        String type
) {
}
