package com.maximilian.es.controller;

import com.maximilian.es.aggregate.Customer;
import com.maximilian.es.command.BalanceChangeCommand;
import com.maximilian.es.command.CreateCustomerCommand;
import com.maximilian.es.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/customer")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public Customer createCustomer(@RequestBody @Valid CreateCustomerCommand command) {
        return customerService.createCustomer(command);
    }

    @PatchMapping("/{id}")
    public Customer updateBalance(@PathVariable Long id,
                                  @RequestBody @Valid BalanceChangeCommand command) {
        return customerService.updateBalance(id, command);
    }

}
