package com.freelance.invoice.repository;

import com.freelance.invoice.domain.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {}
