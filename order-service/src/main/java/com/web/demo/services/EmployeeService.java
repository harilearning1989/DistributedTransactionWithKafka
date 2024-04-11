package com.web.demo.services;

import com.web.demo.models.Address;
import com.web.demo.models.Employee;

import java.util.List;

public interface EmployeeService {
    Address addEmployee(Address address);

    Address saveEmployeeNoTran(Address address);

    Address saveEmployeeRollBack(Address address);

    List<Employee> saveEmployeeList();

    List<Employee> saveEmployeeListNull();

    List<Employee> saveEmployeeListForLoop();

    void insertEmployee(Employee emp);
    void deleteEmployeeById(Integer empid);
}
