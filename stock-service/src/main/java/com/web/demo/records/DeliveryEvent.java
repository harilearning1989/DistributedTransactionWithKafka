package com.web.demo.records;

public record DeliveryEvent(
        CustomerOrder customerOrder,
        String type
) {
}
