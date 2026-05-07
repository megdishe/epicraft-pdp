package com.freelance.invoice.controller;

import com.freelance.invoice.domain.Company;
import com.freelance.invoice.repository.CompanyRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyRepository repository;
    public CompanyController(CompanyRepository repository) { this.repository = repository; }
    @PostMapping public Company create(@RequestBody Company company) { return repository.save(company); }
    @GetMapping public List<Company> findAll() { return repository.findAll(); }
    @GetMapping("/{id}") public Company findById(@PathVariable String id) { return repository.findById(id).orElseThrow(); }
    @PutMapping("/{id}") public Company update(@PathVariable String id, @RequestBody Company company) { return repository.save(new Company(id, company.name(), company.address(), company.email(), company.phone(), company.taxId(), company.bankDetails())); }
    @DeleteMapping("/{id}") public void delete(@PathVariable String id) { repository.deleteById(id); }
}
