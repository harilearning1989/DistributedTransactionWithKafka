package com.web.demo.controls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.demo.models.Delivery;
import com.web.demo.records.CustomerOrder;
import com.web.demo.records.DeliveryEvent;
import com.web.demo.repos.DeliveryRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("delivery")
public class DeliveryRestController {

    private KafkaTemplate<String, DeliveryEvent> deliveryKafkaTemplate;
    private DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryRestController setDeliveryKafkaTemplate(KafkaTemplate<String, DeliveryEvent> deliveryKafkaTemplate) {
        this.deliveryKafkaTemplate = deliveryKafkaTemplate;
        return this;
    }

    @Autowired
    public DeliveryRestController setDeliveryRepository(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
        return this;
    }

    @KafkaListener(topics = "NEW-STOCK", groupId = "STOCK-GROUP")
    public void updateDelivery(ConsumerRecord stockEvent) throws Exception {
        System.out.println("DeliveryRestController updateDelivery::" + stockEvent);
        DeliveryEvent de = DeliveryEvent.class.cast(stockEvent.value());
        CustomerOrder customerOrder = de.customerOrder();

        Delivery delivery = new Delivery();
        try {
            if(customerOrder.address() == null){
                throw new Exception("Address not found exception");
            }
            delivery.setAddress(customerOrder.address());
            delivery.setStatus("Success");
            delivery.setOrderId(customerOrder.orderId());

            deliveryRepository.save(delivery);
        } catch (Exception e) {
            System.out.println("Exception::" + e.getMessage());
            delivery.setStatus("Failed");
            delivery.setOrderId(customerOrder.orderId());
            deliveryRepository.save(delivery);
            System.out.println(customerOrder);

            DeliveryEvent deliveryEvent = new DeliveryEvent(customerOrder,"STOCK-REVERSED");
            deliveryKafkaTemplate.send("STOCK-REVERSED",deliveryEvent);
        }
    }
}
