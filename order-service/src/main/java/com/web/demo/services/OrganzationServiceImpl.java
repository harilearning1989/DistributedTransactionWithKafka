package com.web.demo.services;

import com.web.demo.models.Employee;
import com.web.demo.models.EmployeeHealthInsurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganzationServiceImpl implements OrganizationService {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    HealthInsuranceService healthInsuranceService;

    @Override
    public void joinOrganization(Employee employee, EmployeeHealthInsurance employeeHealthInsurance) {
        employeeService.insertEmployee(employee);
        healthInsuranceService.registerEmployeeHealthInsurance(employeeHealthInsurance);
    }

    @Override
    public void leaveOrganization(Employee employee, EmployeeHealthInsurance employeeHealthInsurance) {
        employeeService.deleteEmployeeById(employee.getId());
        healthInsuranceService.deleteEmployeeHealthInsuranceById(employeeHealthInsurance.getEmpId());
    }

    //@Transactional(propagation= Propagation.REQUIRES_NEW)
    //@Transactional(propagation=Propagation.NEVER)
    //@Transactional(propagation=Propagation.MANDATORY)
    //@Transactional(propagation=Propagation.NOT_SUPPORTED)
    //@Transactional(propagation=Propagation.SUPPORTS)
    @Transactional(propagation=Propagation.REQUIRED)
    public void joinOrganizationException(Employee employee, EmployeeHealthInsurance employeeHealthInsurance) {
        employeeService.insertEmployee(employee);
        if (employee.getId()>12) {
            throw new RuntimeException("thowing exception to test transaction rollback");
        }
        healthInsuranceService.registerEmployeeHealthInsurance(employeeHealthInsurance);
    }
}