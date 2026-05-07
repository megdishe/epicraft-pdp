package com.freelance.invoice.controller;

import com.freelance.invoice.domain.Customer;
import com.freelance.invoice.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository repository;
    public CustomerController(CustomerRepository repository) { this.repository = repository; }
    @PostMapping public Customer create(@RequestBody Customer customer) { return repository.save(customer); }
    @GetMapping public List<Customer> findAll() { return repository.findAll(); }
    @GetMapping("/{id}") public Customer findById(@PathVariable String id) { return repository.findById(id).orElseThrow(); }
    @PutMapping("/{id}") public Customer update(@PathVariable String id, @RequestBody Customer customer) { return repository.save(new Customer(id, customer.name(), customer.address(), customer.email(), customer.phone(), customer.taxId())); }
    @DeleteMapping("/{id}") public void delete(@PathVariable String id) { repository.deleteById(id); }
}
