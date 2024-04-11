package com.web.demo.controls;

import com.web.demo.models.Address;
import com.web.demo.models.Employee;
import com.web.demo.records.CustomerOrder;
import com.web.demo.services.EmployeeService;
import com.web.demo.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
public class OrderRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRestController.class);

    private EmployeeService employeeService;

    private OrderService orderService;

    @Autowired
    public OrderRestController setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
        return this;
    }
    @Autowired
    public OrderRestController setOrderService(OrderService orderService) {
        this.orderService = orderService;
        return this;
    }

    @PostMapping("createOrder")
    public String createOrder(@RequestBody CustomerOrder customerOrder){
        orderService.createOrder(customerOrder);
        return "Order Placed";
    }

    @GetMapping("saveEmpProp")
    public Address saveEmpProp(@RequestBody Address address) {
        return employeeService.addEmployee(address);
    }

    @GetMapping("saveEmployee")
    public Address saveEmployee(@RequestBody Address address) {
        return employeeService.addEmployee(address);
    }

    @GetMapping("saveEmployeeNoTran")
    public Address saveEmployeeNoTran(@RequestBody Address address) {
        return employeeService.saveEmployeeNoTran(address);
    }

    @GetMapping("saveEmpRoll")
    public Address saveEmployeeRollBack(@RequestBody Address address) {
        return employeeService.saveEmployeeRollBack(address);
    }

    @GetMapping("saveEmpList")
    public List<Employee> saveEmployeeList() {
        return employeeService.saveEmployeeList();
    }

    @GetMapping("saveEmpListNull")
    public List<Employee> saveEmployeeListNull() {
        return employeeService.saveEmployeeListNull();
    }

    @GetMapping("saveEmployeeListForLoop")
    public List<Employee> saveEmployeeListForLoop() {
        return employeeService.saveEmployeeListForLoop();
    }

}
