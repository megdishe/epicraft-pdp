package com.epicraft.pdp.invoice.repository;

import com.epicraft.pdp.invoice.domain.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {}
