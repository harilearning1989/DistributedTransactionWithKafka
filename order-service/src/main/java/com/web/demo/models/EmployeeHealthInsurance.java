package com.web.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="EMPLOYEE_INSURANCE")
public class EmployeeHealthInsurance {

    @Id
    @GeneratedValue
    @Column(name = "EMP_ID")
    private String empId;
    @Column(name = "SCHEME")
    private String healthInsuranceSchemeName;
    @Column(name = "COVERAGE")
    private int coverageAmount;

}
