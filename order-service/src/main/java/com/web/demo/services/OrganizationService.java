package com.web.demo.services;

import com.web.demo.models.Employee;
import com.web.demo.models.EmployeeHealthInsurance;

public interface OrganizationService {

    void joinOrganization(Employee employee, EmployeeHealthInsurance employeeHealthInsurance);

    void leaveOrganization(Employee employee, EmployeeHealthInsurance employeeHealthInsurance);

}
