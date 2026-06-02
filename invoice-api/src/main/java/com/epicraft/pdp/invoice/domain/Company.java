package com.epicraft.pdp.invoice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("companies")
public record Company(@Id String id, String name, String address, String email, String phone,String siret, String taxId, BankDetails bankDetails) {}
