package com.web.demo.services;

import com.web.demo.models.Order;
import com.web.demo.records.CustomerOrder;
import com.web.demo.records.OrderEvent;
import com.web.demo.repos.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    public OrderServiceImpl setKafkaTemplate(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        return this;
    }

    @Autowired
    public OrderServiceImpl setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        return this;
    }

    @Override
    public void createOrder(CustomerOrder customerOrder) {
        Order.OrderBuilder orderBuilder = Order.builder();
        orderBuilder.orderName(customerOrder.name())
                .item(customerOrder.item())
                .quantity(customerOrder.quantity())
                .amount(customerOrder.amount())
                .paymentMethod(customerOrder.paymentMethod())
                .address(customerOrder.address())
                .status("Created");

        Order order = orderBuilder.build();
        try {
            order = orderRepository.save(order);
            customerOrder = new CustomerOrder(
                    order.getOrderId(),
                    order.getOrderName(),
                    order.getItem(),
                    order.getQuantity(),
                    order.getAmount(),
                    order.getPaymentMethod(),
                    order.getAddress()
            );

            OrderEvent orderEvent = new OrderEvent(
                    customerOrder,
                    "ORDER_CREATED"
            );
            kafkaTemplate.send("NEW-ORDER", orderEvent);
        } catch (Exception e) {
            order.setStatus("Failed");
            orderRepository.save(order);
        }

    }
}
