package com.web.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PAYMENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private int paymentId;
    @Column(name = "MODE")
    private String mode;
    @Column(name = "ORDER_ID")
    private int orderId;
    @Column(name = "AMOUNT")
    private double amount;
    @Column(name = "STATUS")
    private String status;

}
