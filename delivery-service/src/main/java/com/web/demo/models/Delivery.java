package com.web.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="DELIVERY")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DELIVERY_ID")
    private int deliveryId;
    @Column(name = "ORDER_ID")
    private int orderId;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "STATUS")
    private String status;
}
