package com.web.demo.services;

import com.web.demo.models.Address;
import com.web.demo.models.Employee;
import com.web.demo.repos.AddressRepository;
import com.web.demo.repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    //Propagation Starts here


    @Transactional
    //@Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = ArithmeticException.class,timeout = 10,propagation = Propagation.REQUIRED)
    @Override
    public Address addEmployee(Address address) {
        Employee employee = address.getEmployee();

        employee = this.employeeRepository.save(employee);
        address.setEmployee(employee);

        address = this.addressRepository.save(address);
        System.out.println("address::" + address);
        return address;
    }

    //Without Transaction
    @Override
    public Address saveEmployeeNoTran(Address address) {
        Employee employee = address.getEmployee();
        employee = this.employeeRepository.save(employee);

        System.out.println(10 / 0);
        address.setEmployee(employee);
        address = this.addressRepository.save(address);
        System.out.println("address::" + address);
        return address;
    }

    @Override
    @Transactional(rollbackFor = ArithmeticException.class,
            noRollbackFor = {NullPointerException.class, ArrayIndexOutOfBoundsException.class})
    public Address saveEmployeeRollBack(Address address) {
        Employee employee = address.getEmployee();
        employee = this.employeeRepository.save(employee);
        String str = null;
        System.out.println(str.toLowerCase());
        System.out.println(10 / 0);

        address.setEmployee(employee);

        address = this.addressRepository.save(address);
        System.out.println("address::" + address);
        return address;
    }

    @Override
    @Transactional(timeout = 2)//time out 2 seconds
    public List<Employee> saveEmployeeList() {
        IntStream.range(1, 100000)
                .forEach(f -> {
                    Employee employee = new Employee();
                    employee.setName("Hari " + f);
                    this.employeeRepository.save(employee);
                });
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList;
    }

    @Override
    @Transactional
    public List<Employee> saveEmployeeListForLoop() {
        IntStream.range(1, 100)
                .forEach(f -> {
                    Employee employee = new Employee();
                    employee.setName("Hari " + f);
                    saveEmployeeListForLoopTemp(employee);
                });
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList;
    }

    @Transactional(propagation = Propagation.NEVER)
    public void saveEmployeeListForLoopTemp(Employee employee) {
        if(employee.getName().equalsIgnoreCase("Hari 10")){
            throw new RuntimeException("");
        }
        this.employeeRepository.save(employee);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public List<Employee> saveEmployeeListNull() {
        try {
            IntStream.range(1, 10)
                    .forEach(f -> {
                        Employee employee = new Employee();
                        employee.setName("Hari " + f);
                        if (f == 6) {
                            throw new RuntimeException("Exception");
                        }
                        this.employeeRepository.save(employee);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeRepository.findAll();
    }

    @Override
    public void insertEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(Integer empid) {
        employeeRepository.deleteById(empid);
    }

}
