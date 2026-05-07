package com.epicraft.pdp.invoice.repository;

import com.epicraft.pdp.invoice.domain.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {}
