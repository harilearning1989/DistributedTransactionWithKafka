package com.web.demo.controls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.demo.models.WareHouse;
import com.web.demo.records.*;
import com.web.demo.repos.StockRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("stock")
public class StockRestController {

    private KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate;
    private KafkaTemplate<String, DeliveryEvent> deliveryKafkaTemplate;
    private StockRepository stockRepository;

    @Autowired
    public StockRestController setPaymentKafkaTemplate(KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate) {
        this.paymentKafkaTemplate = paymentKafkaTemplate;
        return this;
    }

    @Autowired
    public StockRestController setDeliveryKafkaTemplate(KafkaTemplate<String, DeliveryEvent> deliveryKafkaTemplate) {
        this.deliveryKafkaTemplate = deliveryKafkaTemplate;
        return this;
    }

    @Autowired
    public StockRestController setStockRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        return this;
    }

    @KafkaListener(topics = "NEW-PAYMENT", groupId = "PAYMENTS-GROUP")
    public void updateStock(ConsumerRecord paymentEvent) throws Exception {
        System.out.println("StockRestController updateStock::" + paymentEvent);
        PaymentEvent pe = PaymentEvent.class.cast(paymentEvent.value());
        //PaymentEvent pe = new ObjectMapper().readValue(paymentEvent, PaymentEvent.class);
        CustomerOrder customerOrder = pe.customerOrder();
        try {
            Optional<WareHouse> wareHousesOpt =
                    stockRepository.findOneByItemAndQuantityIsGreaterThanEqual(customerOrder.item(),customerOrder.quantity());

            if (!wareHousesOpt.isPresent()) {
                System.out.println("No Stock Available");
                throw new Exception("No Stock Available");
            }

            wareHousesOpt.ifPresent(w -> {
                w.setQuantity(w.getQuantity() - customerOrder.quantity());
                stockRepository.save(w);
            });

            DeliveryEvent deliveryEvent = new DeliveryEvent(customerOrder, "STOCK-UPDATED");
            deliveryKafkaTemplate.send("NEW-STOCK", deliveryEvent);
        } catch (Exception e) {
            System.out.println("Exception::" + e.getMessage());
            PaymentEvent payment = new PaymentEvent(customerOrder, "PAYMENT-REVERSED");
            paymentKafkaTemplate.send("PAYMENT-REVERSED", payment);
        }
    }

    @PostMapping("addStock")
    public void addStock(@RequestBody Stock stock) {
        Optional<WareHouse> wareHousesOpt = stockRepository.findOneByItem(stock.item());
        WareHouse wareHouse;
        if (wareHousesOpt.isPresent()) {
            wareHouse = wareHousesOpt.get();
            wareHouse.setQuantity(wareHouse.getQuantity() - stock.quantity());
        } else {
            wareHouse = new WareHouse();
            wareHouse.setQuantity(stock.quantity());
            wareHouse.setItem(stock.item());
        }
        stockRepository.save(wareHouse);
    }
}
