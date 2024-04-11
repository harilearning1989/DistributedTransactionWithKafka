package com.web.demo.reverse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.demo.models.WareHouse;
import com.web.demo.records.DeliveryEvent;
import com.web.demo.records.PaymentEvent;
import com.web.demo.repos.StockRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StockReverseListener {

    private StockRepository stockRepository;
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Autowired
    public StockReverseListener setKafkaTemplate(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        return this;
    }

    @Autowired
    public StockReverseListener setStockRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        return this;
    }

    @KafkaListener(topics = "STOCK-REVERSED", groupId = "STOCK-GROUP")
    private void reverseStock(ConsumerRecord event) {
        System.out.println("Inside reverse stock event::" + event);
        try {
            DeliveryEvent de = DeliveryEvent.class.cast(event.value());
            Optional<WareHouse> wareHousesOpt = stockRepository.findOneByItem(de.customerOrder().item());

            wareHousesOpt.ifPresent(w -> {
                w.setQuantity(w.getQuantity() + de.customerOrder().quantity());
                stockRepository.save(w);
            });
            PaymentEvent paymentEvent = new PaymentEvent(de.customerOrder(), "PAYMENT-REVERSED");
            kafkaTemplate.send("PAYMENT-REVERSED", paymentEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
