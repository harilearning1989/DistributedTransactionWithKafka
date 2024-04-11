package com.web.demo.controls;

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
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentRestController {

    private PaymentRepository paymentRepository;
    private KafkaTemplate<String,PaymentEvent> paymentKafkaTemplate;
    private KafkaTemplate<String,OrderEvent> orderKafkaTemplate;

    @Autowired
    public PaymentRestController setPaymentKafkaTemplate(KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate) {
        this.paymentKafkaTemplate = paymentKafkaTemplate;
        return this;
    }
    @Autowired
    public PaymentRestController setOrderKafkaTemplate(KafkaTemplate<String, OrderEvent> orderKafkaTemplate) {
        this.orderKafkaTemplate = orderKafkaTemplate;
        return this;
    }
    @Autowired
    public PaymentRestController setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        return this;
    }

    @KafkaListener(topics = "NEW-ORDER", groupId = "ORDERS-GROUP")
    public void processPayment(ConsumerRecord event)throws Exception {
        System.out.println("PaymentController orderEvent::" + event);
        OrderEvent orderEvent = OrderEvent.class.cast(event.value());
        CustomerOrder customerOrder = orderEvent.customerOrder();

        Payment.PaymentBuilder paymentBuilder = Payment.builder();
        paymentBuilder.amount(customerOrder.amount())
                .mode(customerOrder.paymentMethod())
                .orderId(customerOrder.orderId())
                .status("Created");
        Payment payment = paymentBuilder.build();
        try {
            payment = paymentRepository.save(payment);

            PaymentEvent paymentEvent = new PaymentEvent(customerOrder,"PAYMENT-CREATED");
            paymentKafkaTemplate.send("NEW-PAYMENT",paymentEvent);
        }catch (Exception e){
            System.out.println("Exception::"+e.getMessage());
            payment.setOrderId(customerOrder.orderId());
            payment.setStatus("Failed");

            paymentRepository.save(payment);

            OrderEvent oe = new OrderEvent(customerOrder,"ORDER-REVERSED");
            orderKafkaTemplate.send("ORDER-REVERSED",oe);
        }
    }
}
