package com.web.demo.services;

import com.web.demo.models.EmployeeHealthInsurance;

public interface HealthInsuranceService {

    void registerEmployeeHealthInsurance(EmployeeHealthInsurance employeeHealthInsurance);
    void deleteEmployeeHealthInsuranceById(String empid);
}
