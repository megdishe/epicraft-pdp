package com.freelance.invoice.repository;

import com.freelance.invoice.domain.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {}
