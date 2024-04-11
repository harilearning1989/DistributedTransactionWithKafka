package com.web.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ORDER_DETAIL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDERID")
    private int orderId;
    @Column(name = "ORDER_NAME")
    private String orderName;
    @Column(name = "ITEM")
    private String item;
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "AMOUNT")
    private double amount;
    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "STATUS")
    private String status;
}
