package com.web.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="EMPLOYEE_TEMP")
public class Employee {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;

}