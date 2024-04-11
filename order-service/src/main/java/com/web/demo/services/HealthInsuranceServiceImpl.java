package com.web.demo.services;

import com.web.demo.models.EmployeeHealthInsurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthInsuranceServiceImpl implements HealthInsuranceService {

    @Override
    public void registerEmployeeHealthInsurance(EmployeeHealthInsurance employeeHealthInsurance) {
        //healthInsuranceDao.registerEmployeeHealthInsurance(employeeHealthInsurance);
    }

    @Override
    public void deleteEmployeeHealthInsuranceById(String empid) {
        //healthInsuranceDao.deleteEmployeeHealthInsuranceById(empid);
    }

}