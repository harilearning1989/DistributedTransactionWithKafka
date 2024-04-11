package com.web.demo.reverse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.demo.models.Payment;
import com.web.demo.records.CustomerOrder;
import com.web.demo.records.OrderEvent;
import com.web.demo.records.PaymentEvent;
import com.web.demo.repos.PaymentRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentReverseListener {

    private PaymentRepository paymentRepository;
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    public PaymentReverseListener setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        return this;
    }

    @Autowired
    public PaymentReverseListener setKafkaTemplate(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        return this;
    }

    @KafkaListener(topics = "PAYMENT-REVERSED", groupId = "PAYMENT-GROUP")
    private void reversePayment(ConsumerRecord paymentEvent) {
        System.out.println("Inside reverse payment event::" + paymentEvent);
        try {
            PaymentEvent pe = PaymentEvent.class.cast(paymentEvent.value());
            CustomerOrder customerOrder = pe.customerOrder();
            Iterable<Payment> payments = paymentRepository.findByOrderId(customerOrder.orderId());
            payments.forEach(f -> {
                f.setStatus("Failed");
                paymentRepository.save(f);
            });

            OrderEvent orderEvent = new OrderEvent(
                    customerOrder, "ORDER-REVERSED"
            );
            kafkaTemplate.send("ORDER-REVERSED", orderEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
