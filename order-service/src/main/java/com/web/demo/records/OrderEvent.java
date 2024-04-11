package com.web.demo.records;

public record OrderEvent(
        CustomerOrder customerOrder,
        String type
) {
}
