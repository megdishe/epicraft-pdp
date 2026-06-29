package com.epicraft.pdp.invoice.dto;

import java.math.BigDecimal;

public record CreateInvoiceLineRequest(String description, BigDecimal quantity, BigDecimal unitPrice) {}
