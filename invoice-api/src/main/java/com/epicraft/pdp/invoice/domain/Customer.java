package com.epicraft.pdp.invoice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customers")
public record Customer(@Id String id, String name, String address, String email, String phone, String taxId, String siret) {}
