package com.web.demo.services;

import com.web.demo.records.CustomerOrder;

public interface OrderService {
    void createOrder(CustomerOrder customerOrder);
}
