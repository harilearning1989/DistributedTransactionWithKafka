package com.web.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WAREHOUSE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WareHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WAREHOUSE_ID")
    private int wareHouseId;
    @Column(name = "ITEM")
    private String item;
    @Column(name = "QUANTITY")
    private int quantity;

}
