package com.web.demo.reverse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.demo.models.Order;
import com.web.demo.records.OrderEvent;
import com.web.demo.repos.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderReverseListener {

    private OrderRepository orderRepository;

    @Autowired
    public OrderReverseListener setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        return this;
    }

    @KafkaListener(topics = "ORDER-REVERSED",groupId = "ORDERS-GROUP")
    public void rollBackOrder(ConsumerRecord event) {
        System.out.println("event::" + event);
        try {
            OrderEvent orderEvent = OrderEvent.class.cast(event.value());
            Optional<Order> optionalOrder = orderRepository.findById(orderEvent.customerOrder().orderId());

            optionalOrder.ifPresent(order -> {
                order.setStatus("Failed");
                orderRepository.save(order);
            });
        }catch (Exception e){
            System.out.println("Exception::"+e.getMessage());
        }
    }
}
